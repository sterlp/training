package org.sterl.jpa.strong_id.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sterl.jpa.strong_id.model.Vehicle;
import org.sterl.jpa.strong_id.model.Vin;

public interface VehicleRepository extends JpaRepository<Vehicle, Vin> {

}
