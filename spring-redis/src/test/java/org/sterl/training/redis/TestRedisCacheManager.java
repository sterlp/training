package org.sterl.training.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sterl.training.redis.entity.CachedEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestRedisCacheManager {
    @Autowired
    ObjectHashMapper objectMapper;
    
    @Autowired
    RedisConnectionFactory connectionFactory;

    @BeforeEach
    void setUp() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            connection.flushAll();
        };
    }
    
    @Test
    void test() throws Exception {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.java()));
        redisCacheConfiguration.usePrefix();

        final RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
        
        assertThat(cacheManager.getCache("mycache").get("key1")).isNull();

        final CachedEntity entry = CachedEntity.builder()
                .id("Muster_id")
                .payload("Muster")
                .cacheTime(Instant.now())
                .build();

        cacheManager.getCache("mycache").put("key1", entry);
        
        assertThat(cacheManager.getCache("mycache").get("key1")).isNotNull();
        assertThat(
                ((ValueWrapper)cacheManager.getCache("mycache").get("key1")).get())
                .isEqualTo(entry);
    }

}
