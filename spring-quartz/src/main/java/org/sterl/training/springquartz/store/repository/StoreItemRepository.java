package org.sterl.training.springquartz.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sterl.training.springquartz.store.model.StoreItemBE;

public interface StoreItemRepository extends JpaRepository<StoreItemBE, Long> {

}
