package org.sterl.jpa.strong_id.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sterl.jpa.strong_id.model.Engine;
import org.sterl.jpa.strong_id.model.EngineId;

public interface EngineRepostory extends JpaRepository<Engine, EngineId> {

}
