package org.sterl.training.axon.toy.component;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sterl.training.axon.toy.model.ToyEntity;

public interface ToyRepository extends JpaRepository<ToyEntity, String> {

}
