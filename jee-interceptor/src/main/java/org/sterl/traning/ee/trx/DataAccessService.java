package org.sterl.traning.ee.trx;

import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.sterl.traning.ee.trx.timeout.TransactionTimeout;

/**
 * @author sterlp
 */
@ApplicationScoped
public class DataAccessService {
    @PersistenceContext private EntityManager em;
    
    @TransactionTimeout(1)
    @Transactional(Transactional.TxType.REQUIRED)
    public int generateData() {
        
        System.out.println("Hints: " + em.createQuery("SELECT COUNT(e) FROM FooEntity e").getHints());        
        int count = ((Number)em.createQuery("SELECT COUNT(e) FROM FooEntity e").getSingleResult()).intValue();
        em.persist(new FooEntity().setName(UUID.randomUUID().toString()));
        em.createNativeQuery("SELECT 1  FROM pg_sleep(2)")
                //.setHint("eclipselink.jdbc.timeout", 1)
                .getResultList();
        
        return count;
    }
}
