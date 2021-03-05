package org.sterl.training.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Optional;

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
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sterl.training.redis.data.dao.CacheEntityDao;
import org.sterl.training.redis.entity.CachedEntity;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


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
    
    @Autowired 
    ReactiveStringRedisTemplate reactiveRedisStringTemplate;
    
    @Autowired
    ObjectHashMapper mapper;
    
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
    
    
    // Use the reactive template in a compatible way to spring data
    @Test
    void testReactiveString() {
        
        final ReactiveHashOperations<String, Object, Object> opsForHash = reactiveRedisStringTemplate.opsForHash();

        Flux.just("Max", "Muster")
            .map(s -> CachedEntity.builder()
                    .id(s + "_id")
                    .payload(s)
                    .cacheTime(Instant.now())
                    .build())
            .flatMap(e -> opsForHash.putAll("entity:" + e.getId(), mapper.toHash(e)))
            .blockLast();
        
        final Optional<CachedEntity> springData = cacheEntityDao.findById("Muster_id");
        assertThat(springData.isPresent()).isTrue();
        assertThat(springData.get().getPayload()).isEqualTo("Muster");

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
            .flatMap(e -> opsForHash. put("entity", e.getId(), e));
            
        StepVerifier.create(savePublisher)
            .expectNext(Boolean.TRUE)
            .expectNext(Boolean.TRUE)
            .verifyComplete();
        

        opsForHash.values("entity")
            .subscribe(System.out::println);

        StepVerifier.create(opsForHash.get("entity", "Max_id"))
            .expectNextMatches(ce -> ce.getPayload().equals("Max"))
            .verifyComplete();
        
        
        StepVerifier.create(opsForHash.values("entity").collectList())
            .expectNextMatches(el -> el.size() == 2)
            .verifyComplete();
    }
}
