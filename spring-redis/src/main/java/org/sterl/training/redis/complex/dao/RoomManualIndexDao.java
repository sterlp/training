package org.sterl.training.redis.complex.dao;

import org.springframework.data.repository.CrudRepository;
import org.sterl.training.redis.complex.model.RoomManualIndexBE;

public interface RoomManualIndexDao extends CrudRepository<RoomManualIndexBE, String> {
}
