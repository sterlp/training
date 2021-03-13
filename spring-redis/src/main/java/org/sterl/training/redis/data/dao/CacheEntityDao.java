package org.sterl.training.redis.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.sterl.training.redis.model.CachedEntity;

public interface CacheEntityDao extends CrudRepository<CachedEntity, String> {

}
