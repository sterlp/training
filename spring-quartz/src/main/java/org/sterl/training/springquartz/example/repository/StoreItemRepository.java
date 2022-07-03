package org.sterl.training.springquartz.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sterl.training.springquartz.example.model.StoreItemBE;

public interface StoreItemRepository extends JpaRepository<StoreItemBE, Long> {

}
