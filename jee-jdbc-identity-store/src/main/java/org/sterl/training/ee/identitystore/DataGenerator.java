package org.sterl.training.ee.identitystore;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Startup
@Singleton
public class DataGenerator {

    @PersistenceContext private EntityManager em;
    
    @PostConstruct
    public void init() {
        for (int i = 1; i <= 50; ++i) {
            em.persist(new SampleEntity().setName("Name " + i));
        }
    }
}
