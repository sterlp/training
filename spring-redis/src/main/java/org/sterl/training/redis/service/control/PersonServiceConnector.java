package org.sterl.training.redis.service.control;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.sterl.training.redis.entity.CachedEntity;
import org.sterl.training.redis.service.entity.Person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *  Service which we will mock in the test, just for demonstration
 *  it represents the slow service we want to cache with redis.
 */
@Slf4j
@Service
public class PersonServiceConnector {

    @Autowired SleeperBA sleeperBA;
    @Autowired ObjectMapper mapper;
    @Autowired ReactiveRedisTemplate<String, CachedEntity> reactiveRedisTemplate;

    public Mono<String> load(String id) {
        return sleeperBA
            .execute(2500)
            .map(v -> {
                log.info("Creating new person");
                return new Person(id, UUID.randomUUID().toString());
            })
            .map(p -> {
                try {
                    log.info("Converting {}", p);
                    return mapper.writeValueAsString(p);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
    }
    
    public Mono<CachedEntity> loadAndCache(String id) {
        final Mono<CachedEntity> publisher = Mono.just(id)
            .flatMap(this::load)
            .map(s -> new CachedEntity("entity:" + id, s, Instant.now()));
        
        final Mono<CachedEntity> shared = publisher.share();
        
        Mono.from(shared)
            .flatMap(c -> reactiveRedisTemplate.opsForValue().set(c.getId(), c))
            .subscribe(
                b -> log.info("Saved: {} - {}", id, shared.block()),
                e -> log.error("Failed to save {}.", id, e)
            );

        return shared;
    }
}
