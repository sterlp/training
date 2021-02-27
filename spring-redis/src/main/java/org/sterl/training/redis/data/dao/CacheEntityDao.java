package org.sterl.training.redis.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.sterl.training.redis.entity.CachedEntity;

public interface CacheEntityDao extends CrudRepository<CachedEntity, String> {

}
