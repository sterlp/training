package org.sterl.training.redis.complex.dao;

import org.springframework.data.repository.CrudRepository;
import org.sterl.training.redis.complex.model.RoomBE;

public interface RoomDao extends CrudRepository<RoomBE, String> {

}
