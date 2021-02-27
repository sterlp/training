package org.sterl.training.redis.data.api;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.training.redis.data.dao.CacheEntityDao;
import org.sterl.training.redis.entity.CachedEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/data")
public class RedisDataResource {

    @Autowired CacheEntityDao cache;
    @Autowired ObjectMapper mapper;

    @GetMapping()
    public Iterable<CachedEntity> all() {
        return cache.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CachedEntity> getByKey(@PathVariable String id) {
        return cache.findById(id);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        cache.deleteById(id);
    }
    
    @PostMapping
    public CachedEntity create(@RequestBody CachedEntity entity) {
        Objects.requireNonNull(entity.getId(), "ID cannot be null.");
        return cache.save(entity);
    }
    
    @PostMapping("/{id}")
    public CachedEntity create(@PathVariable String id, @RequestBody CachedEntity entity) {
        entity.setId(id);
        return create(entity);
    }
}
