package org.sterl.training.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

@SpringBootApplication
@EnableRedisRepositories
@EnableAsync
public class SpringRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisApplication.class, args);
    }
    
    @Bean
    ObjectHashMapper objectMapper() {
        return new ObjectHashMapper();
    }
    
    @Bean
    StringKeySerializer keySerializer() {
        return new StringKeySerializer();
    }
    
}
