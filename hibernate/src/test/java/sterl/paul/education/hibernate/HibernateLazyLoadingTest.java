/*
 * Copyright 2015 sterlp.
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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Lazy loading of entities and hibernate batch fetch size.
 */
public class HibernateLazyLoadingTest {
    
    @Data
    @Entity(name = "Office") @NoArgsConstructor @AllArgsConstructor
    public static class Office {
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        @OneToMany(fetch = FetchType.LAZY)
        List<Employee> workers = new ArrayList<>();
        public Office(String name) {this.name = name;}
    }
    @Data
    @Entity(name = "Employee") @NoArgsConstructor @AllArgsConstructor
    public static class Employee {
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
        @JoinColumn(nullable = false, name = "office_id")
        private Office office;
        public Employee(String name, Office office) {
            this.name = name;
            this.office = office;
        }
    }
    
    private static Configuration cfg;
    private static SessionFactory sessionFactory;
    @BeforeClass
    public static void startHibernate() {
        cfg = new Configuration()
                .addAnnotatedClass(Office.class)
                .addAnnotatedClass(Employee.class)
                .setProperty("hibernate.default_batch_fetch_size", "10")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop");
        
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        sessionFactory = cfg.buildSessionFactory(builder.build());
        
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();
        
        Office office1 = new Office("1st Flor 101");
        Office office2 = new Office("1ed Flor 102");
        Office office3 = new Office("1ed Flor 201");
        Office office4 = new Office("1ed Flor 202");
        Office office5 = new Office("1ed Flor 301");
        session.persist(office1);
        session.persist(office2);
        session.persist(office3);
        session.persist(office4);
        session.persist(office5);
        
        session.persist(new Employee("Albert", office1));
        session.persist(new Employee("Goethe", office2));
        session.persist(new Employee("Newton", office3));
        session.persist(new Employee("Bernoulli", office4));
        session.persist(new Employee("Gauß", office5));
        
        trx.commit();
        session.close();
    }
    
    @Test
    public void showLazyLoading() {
        System.out.println("\n----\n");
        
        Session session = sessionFactory.openSession();
        Transaction trx = session.beginTransaction();
        
        List<Employee> list = session.createQuery("SELECT e FROM Employee e ORDER BY e.name")
                .setMaxResults(20)
                .list();
        
        System.out.println("Employees: " + list.size());
        /*
        Run this to see that hibernate will fetch even the workers again from the DB.
        
        Employee -n:1-> Office -1:n-> Employee -- no frist level cache hit here!!!
        
        Again the batch fetch size helps
        */
        for (Employee e : list) {
            System.out.println(e);
        }
        
    }
    
}
