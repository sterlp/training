package org.sterl.training.redis.service.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.sterl.training.redis.entity.CachedEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ReactiveRedisConfig {
    
    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        JavaTimeModule module = new JavaTimeModule();
        return new Jackson2ObjectMapperBuilder()
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .findModulesViaServiceLoader(true)
            .modulesToInstall(module);
    }
    /*
    @Bean
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder mapperBuilder) {
        return mapperBuilder.build();
    }*/


    @Bean
    public ReactiveRedisTemplate<String, CachedEntity> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory factory, ObjectMapper objectMapper) {
        
        final StringRedisSerializer keySerializer = new StringRedisSerializer();
        final Jackson2JsonRedisSerializer<CachedEntity> valueSerializer =
                new Jackson2JsonRedisSerializer<>(CachedEntity.class);
        valueSerializer.setObjectMapper(objectMapper);

        RedisSerializationContext.RedisSerializationContextBuilder<String, CachedEntity> builder =
          RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, CachedEntity> context = 
          builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
