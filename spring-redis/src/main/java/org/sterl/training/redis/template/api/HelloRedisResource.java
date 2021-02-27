package org.sterl.training.redis.template.api;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/redis")
public class HelloRedisResource {

    @Autowired StringRedisTemplate redis;
    @Autowired ReactiveStringRedisTemplate reactiveRedis;

    @PostConstruct
    void init() {
        flushAll();
    }
    
    @DeleteMapping
    public void flushAll() {
        try (RedisConnection c = redis.getConnectionFactory().getConnection()) {
            c.flushAll();
            log.info("Redis cache cleared!");
        }
    }
    
    @GetMapping
    public Mono<List<String>> get() {
        return reactiveRedis.keys("*").collectList();
    }
    
    @GetMapping("/{key}")
    public Mono<String> getByKey(@PathVariable String key) {
        return reactiveRedis.opsForValue().get(key);
    }
    
    @RequestMapping(path = "/{key}", method = {RequestMethod.POST, RequestMethod.PUT})
    public Mono<Boolean> setKeyValue(@PathVariable String key, @RequestBody String value) {
        return reactiveRedis.opsForValue().set(key, value);
    }

    @PostMapping("/reactive")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> reactive(@RequestBody String request) {
        return Mono.just(request)
                .doOnNext(msg -> reactiveRedis.opsForValue().set("message", msg + " " + Instant.now()))
                .doOnNext((v) -> reactiveRedis.opsForValue().get("message"));
    }
    
    @RequestMapping("/async")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> async(@RequestBody String msg) {
        redis.opsForValue().set("message", msg + " " + Instant.now());
        String result = redis.opsForValue().get("message").toString();
        return CompletableFuture.completedFuture(result);
    }
    
    @RequestMapping("/sync")
    @ResponseStatus(HttpStatus.CREATED)
    public String sync(@RequestBody String msg) {
        redis.opsForValue().set("message", msg + " " + Instant.now());
        String result = redis.opsForValue().get("message").toString();
        return result;
    }
}
