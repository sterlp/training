package org.sterl.training.redis.complex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sterl.training.redis.complex.dao.RoomDao;
import org.sterl.training.redis.complex.model.PersonBE;
import org.sterl.training.redis.complex.model.RoomBE;

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
    void testInsert() {
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

}
