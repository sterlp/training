package org.sterl.training.redis.complex.dao;

import org.springframework.data.repository.CrudRepository;
import org.sterl.training.redis.complex.model.PersonManualIndexBE;

public interface PersonManualIndexDao extends CrudRepository<PersonManualIndexBE, String> {
}
