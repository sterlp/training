/*
 * Copyright 2014 sterlp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sterl.paul.education.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Usage of custom types in Hibernate -- even as ID field and in a query.
 * http://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch06.html#types-custom
 *
 * @author sterlp
 */
public class CustomTypeStrongIdTest {

    @Data
    public static class StrongId implements Serializable {
        final String value;
    }

    /**
     * Very bulky code for Hibernate to handle the ID. Note the StongId is
     * unmutable and Serializable -- thats why most parts of this user type can
     * be handled quite easy. It is more tricky for complexer types.
     */
    static class StrongIdType implements UserType {
        @Override
        public int[] sqlTypes() {
            return new int[StringType.INSTANCE.sqlType()];

        }
        @Override
        public Class returnedClass() {
            return StrongIdType.class;
        }

        @Override
        public boolean equals(Object x, Object y) throws HibernateException {
            if (x == y) {
                return true;
            }
            if (x == null) {
                return false;
            }
            if (y == null) {
                return false;
            }
            return x.equals(y);
        }

        @Override
        public int hashCode(Object x) throws HibernateException {
            return x.hashCode();
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            // names array 
            if (names == null || names.length == 0 || names[0] == null) {
                return null;
            }
            Object value = StringType.INSTANCE.get(rs, names[0], session);
            if (value == null) {
                return null;
            }
            return new StrongId(value.toString());
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            if (value == null) {
                StringType.INSTANCE.set(st, null, index, session);
            } else {
                final StrongId id = (StrongId) value;
                StringType.INSTANCE.set(st, id.getValue(), index, session);
            }
        }

        @Override
        public Object deepCopy(Object value) throws HibernateException {
            return new StrongId(value.toString());
        }

        @Override
        public boolean isMutable() {
            return false;
        }

        @Override
        public Serializable disassemble(Object value) throws HibernateException {
            return (StrongId) value;
        }

        @Override
        public Object assemble(Serializable cached, Object owner) throws HibernateException {
            return cached; // it is not mutable, otherwis we would need a copy
        }

        @Override
        public Object replace(Object original, Object target, Object owner) throws HibernateException {
            return original;
        }
    }


    @Entity(name = "FooBE")
    @Table(name = "FOO")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class FooBE {
        @Id
        StrongId id; // custom type as ID
        String name;
        StrongId someValue;
    }

    static SessionFactory sessionFactory;

    @BeforeClass
    public static void startHibernate() {
        Configuration cfg = new Configuration()
                .addAnnotatedClass(FooBE.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    @Test
    public void testCustomType() {
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();

        session.persist(new FooBE(new StrongId("1"), "Foo1", null));
        session.persist(new FooBE(new StrongId("2"), "Foo2", new StrongId("abc")));
        session.persist(new FooBE(new StrongId("3"), "Foo2", new StrongId("fooBar")));

        trx.commit();
        session.close();

        session = sessionFactory.openSession();
        trx = session.beginTransaction();

        Query q = session.createQuery("SELECT f from FooBE f where f.id = :id");
        q.setParameter("id", new StrongId("2"));
        FooBE f = (FooBE) q.uniqueResult();

        trx.rollback();
        session.close();

        assertEquals(new StrongId("2"), f.getId());
        assertEquals("Foo2", f.getName());

        // change the ID value
        session = sessionFactory.openSession();
        trx = session.beginTransaction();

        q = session.createQuery("SELECT f from FooBE f where f.id = :id");
        q.setParameter("id", new StrongId("2"));
        f = (FooBE) q.uniqueResult();
        f.setSomeValue(new StrongId("17"));

        trx.commit();
        session.close();

        // new trx
        session = sessionFactory.openSession();
        trx = session.beginTransaction();

        q = session.createQuery("SELECT f from FooBE f where f.id = :id");
        q.setParameter("id", new StrongId("2"));
        f = (FooBE) q.uniqueResult();

        trx.rollback();
        session.close();

        assertEquals(new StrongId("17"), f.getSomeValue());
    }
}
