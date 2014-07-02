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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

public class UsingListsTest {
    // Entities which have a uni relation -- not biderectional
    @Entity
    @Table(name = "PERSON")
    @Data
    @NoArgsConstructor
    static class PersonBE {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;

        public PersonBE(String name) {
            this.name = name;
        }
    }

    @Entity
    @Table(name = "OFFICE")
    @Data
    @NoArgsConstructor
    private static class OfficeBE {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JoinColumn(name = "room_id")
        private List<PersonBE> employees = new ArrayList<PersonBE>();

        public OfficeBE(String name) {
            this.name = name;
        }
    }

    // Entities which have a bidirectional binding and are lazy loading
    @Entity
    @Table(name = "ITEM_BE")
    @Data
    @NoArgsConstructor
    static class ItemBE {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        @ManyToOne(cascade = {})
        @JoinColumn(name = "order_id")
        private OrderBE order;

        public ItemBE(String name, OrderBE order) {
            this.name = name;
            this.order = order;
        }
    }

    @Entity
    @Table(name = "ORDER_BE") // ORDER is a resevered keyword in H2 DB
    @Data
    @NoArgsConstructor
    private static class OrderBE {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id")
        private List<ItemBE> items = new ArrayList<ItemBE>();

        public OrderBE(String name) {
            this.name = name;
        }
    }

    static SessionFactory sessionFactory;

    @BeforeClass
    public static void startHibernate() {
        Configuration cfg = new Configuration()
                .addAnnotatedClass(PersonBE.class)
                .addAnnotatedClass(OfficeBE.class)
                .addAnnotatedClass(OrderBE.class)
                .addAnnotatedClass(ItemBE.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop");

        sessionFactory = cfg.buildSessionFactory();
    }

    @Test
    public void testListPersistenceUniBindingWrong() {
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();

        OfficeBE oberhachingOffice = new OfficeBE("Oberhaching");
        oberhachingOffice.getEmployees().add(new PersonBE("Paul"));

        session.persist(oberhachingOffice);
        trx.commit();
        session.close();

        session = sessionFactory.openSession();
        trx = session.beginTransaction();
        oberhachingOffice = (OfficeBE) session.get(OfficeBE.class, oberhachingOffice.getId());
        
        System.out.println("*** You see one insert, but you will see one insert and THREE updates on the PERSON table ***");
        System.out.flush();
        // copy the list
        ArrayList<PersonBE> newPersons = new ArrayList<>(oberhachingOffice.getEmployees());
        newPersons.add(new PersonBE("Monika"));
        
        oberhachingOffice.setEmployees(newPersons);
        
        trx.commit();
        session.close();

    }

    @Test
    public void testListPersistenceUniBindingRight() {
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();

        OfficeBE oberhachingOffice = new OfficeBE("Oberhaching");
        oberhachingOffice.getEmployees().add(new PersonBE("Paul"));

        session.persist(oberhachingOffice);
        trx.commit();
        session.close();

        session = sessionFactory.openSession();
        trx = session.beginTransaction();
        oberhachingOffice = (OfficeBE) session.get(OfficeBE.class, oberhachingOffice.getId());

        System.out.println("*** You would expect one insert, and one update -- which is where because we have a unidirectional Link! ***");
        System.out.flush();

        // copy the list
        List<PersonBE> newPersons = oberhachingOffice.getEmployees();
        newPersons.add(new PersonBE("Monika"));

        trx.commit();
        session.close();
    }

    @Test
    public void testListPersistenceBidirectionalBindingWrong() {
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();

        OrderBE order = new OrderBE("Oberhaching");
        order.getItems().add(new ItemBE("Harry Potter", order));

        session.persist(order);
        trx.commit();
        session.close();

        session = sessionFactory.openSession();
        trx = session.beginTransaction();
        order = (OrderBE) session.get(OrderBE.class, order.getId());
       
        System.out.println("*** This will even fail if the set the many to one as , optional = false. You see one insert and n + 1 updated. DOOMS day! ***");
        System.out.flush();

        // copy the list
        ArrayList<ItemBE> newItems = new ArrayList<>(order.getItems());
        newItems.add(new ItemBE("Rexx in Action", order));

        order.setItems(newItems);

        trx.commit();
        session.close();
    }

    @Test
    public void testListPersistenceBidirectionalBindingStillWrong() {
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();

        OrderBE order = new OrderBE("Oberhaching");
        order.getItems().add(new ItemBE("Harry Potter", order));

        session.persist(order);
        trx.commit();
        session.close();

        session = sessionFactory.openSession();
        trx = session.beginTransaction();
        order = (OrderBE) session.get(OrderBE.class, order.getId());

        System.out.println("*** Okay still bad, as we have a select we don't need and an update. We expected only an Insert. ***");
        System.out.flush();

        // copy the list
        order.getItems().add(new ItemBE("Rexx in Action", order));

        trx.commit();
        session.close();
    }

    @Test
    public void testListPersistenceBidirectionalBindingCorrect() {
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();

        OrderBE order = new OrderBE("Oberhaching");
        order.getItems().add(new ItemBE("Harry Potter", order));

        session.persist(order);
        trx.commit();
        session.close();

        session = sessionFactory.openSession();
        trx = session.beginTransaction();
        order = (OrderBE) session.get(OrderBE.class, order.getId());

        System.out.println("*** Now you see Hibernate does what we thing it is doing. One insert thats it. ***");
        System.out.flush();

        // copy the list
        session.persist(new ItemBE("Rexx in Action", order));

        trx.commit();
        session.close();
    }

}
