package org.sterl.training.redis.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.training.redis.model.CachedEntity;
import org.sterl.training.redis.service.control.PersonServiceConnector;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/person/")
public class CachedPersonResource {

    @Autowired ReactiveRedisTemplate<String, CachedEntity> reactiveRedisTemplate;
    @Autowired PersonServiceConnector personSerive;
    
    @GetMapping("{id}")
    public Mono<CachedEntity> getByKey(@PathVariable String id) {
        return reactiveRedisTemplate.opsForValue()
            .get("entity:" + id)
            // ensure that methods aren't called directly, using a wrapper
            .switchIfEmpty(Mono.defer(() -> personSerive.loadAndCache(id)));
        
    }
}
