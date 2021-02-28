package org.sterl.training.redis;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sterl.training.redis.data.dao.CacheEntityDao;
import org.sterl.training.redis.entity.CachedEntity;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RepsoitoryTests {


    @Autowired
    CacheEntityDao cacheEntityDao;
    @Autowired
    RedisKeyValueTemplate template;
    @Autowired
    RedisConnectionFactory connectionFactory;
    @Autowired 
    ReactiveRedisTemplate<String, CachedEntity> reactiveRedisTemplate;
    
    @BeforeEach
    void setUp() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            connection.flushAll();
        };
    }
    
    @Test
    void testRepo() {
        for (int i = 1; i <= 3; i++) {
            cacheEntityDao.save(CachedEntity.builder()
                    .cacheTime(Instant.now())
                    .payload("payload " + i)
                    .build());
        }
        
        cacheEntityDao.save(CachedEntity.builder()
                .id("1")
                .cacheTime(Instant.now())
                .payload("Custom id")
                .build());
        
        assertThat(cacheEntityDao.findAll()).isEqualTo(template.findAll(CachedEntity.class));
        
        reactiveRedisTemplate.keys("*")
            .subscribe(System.out::println);
        
       
        
        
    }
    
    @Test
    void testReactive() {
        final ReactiveHashOperations<String, String, CachedEntity> opsForHash 
            = reactiveRedisTemplate.opsForHash();
        
        final Flux<Boolean> savePublisher = Flux.just("Max", "Muster")
            .map(s -> CachedEntity.builder()
                    .id(s + "_id")
                    .payload(s).cacheTime(Instant.now())
                    .build())
            .flatMap(e -> opsForHash.put(e.getId(), "entity", e));
            
        StepVerifier.create(savePublisher)
            .expectNext(Boolean.TRUE)
            .expectNext(Boolean.TRUE)
            .verifyComplete();
        
        opsForHash.keys("*")
            .subscribe(System.out::println);
        
        StepVerifier.create(opsForHash.keys("*").count())
            .expectNextCount(2)
            .expectComplete()
            .verify();

        StepVerifier.create(opsForHash.get("Max_id", CachedEntity.class))
            .expectNextMatches(ce -> ce.getPayload().equals("Max"))
            .expectComplete()
            .verify();
        
        
    }
}
