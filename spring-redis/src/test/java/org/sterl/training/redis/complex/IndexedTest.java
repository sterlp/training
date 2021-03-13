package org.sterl.training.redis.complex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sterl.training.redis.complex.dao.PersonManualIndexDao;
import org.sterl.training.redis.complex.dao.RoomDao;
import org.sterl.training.redis.complex.dao.RoomManualIndexDao;
import org.sterl.training.redis.complex.model.PersonBE;
import org.sterl.training.redis.complex.model.PersonManualIndexBE;
import org.sterl.training.redis.complex.model.RoomBE;
import org.sterl.training.redis.complex.model.RoomManualIndexBE;

/**
 * This test shows the speed of @Indexed in spring data.
 * 
 * Attention: The performance is limited and based on the user case it is maybe not the right thing to use.
 * 
 * @author sterlp
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class IndexedTest {

    @Autowired RoomDao roomDao;
    
    @Autowired RoomManualIndexDao roomManualDao;
    @Autowired PersonManualIndexDao personManualDao;

    @Autowired
    RedisConnectionFactory connectionFactory;

    @BeforeEach
    void setUp() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            connection.flushAll();
        };
    }

    /**
     * Enable and disable @Indexed in {@link PersonBE} - run this test to see the time.
     */
    @Test
    void testIndexedInsert() {
        long total = 0;
        for (int i = 1; i < 100; ++i) {
            final RoomBE r = new RoomBE("Room " + i);
            for (int n = 1; n < i + 1; n++) {
                r.addPerson("Person " + n);
            }
            long time = System.currentTimeMillis();
            roomDao.save(r);
            time = System.currentTimeMillis() - time;
            System.out.println(r.getName() + " time: " + time + "ms");
            total += time;
        }
        System.out.println("Total: " + total / 1000 + "s");
        
        long time = System.currentTimeMillis();
        System.out.println(
                roomDao.findByPersonsName("Person 50").size());
        time = System.currentTimeMillis() - time;
        System.out.println("Load Person 50 time: " + time + "ms");
    }
    
    /**
     * Represent more or less the same as above but build manually.
     * 
     * Of course no fair comparison but it should demonstrate "generic" code vs. custom code.
     * --> in this case we have no "Index updates".
     */
    @Test
    void testManualIndex() {
        long total = 0;
        for (int i = 1; i < 100; ++i) {
            final RoomManualIndexBE r = new RoomManualIndexBE("Room " + i);
            for (int n = 1; n < i + 1; n++) {
                r.addPerson("Person " + n);
            }
            
            final PersonManualIndexBE p = new PersonManualIndexBE("Person " + i);
            for (int n = 1; n < i + 1; n++) {
                p.addRoom("Room " + n);
            }
            

            long time = System.currentTimeMillis();
            roomManualDao.save(r);
            personManualDao.save(p);
            time = System.currentTimeMillis() - time;

            System.out.println(r.getName() + " & " + p.getName() + " time: " + time + "ms");
            total += time;
        }
        System.out.println("Total: " + total / 1000 + "s");
        
        long time = System.currentTimeMillis();
        System.out.println(
                personManualDao.findById("Person 50").get().getRooms().size());
        time = System.currentTimeMillis() - time;
        System.out.println("Load Person 50 time: " + time + "ms");
    }

}
