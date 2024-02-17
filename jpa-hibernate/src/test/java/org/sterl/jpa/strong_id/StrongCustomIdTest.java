package org.sterl.jpa.strong_id;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.sterl.jpa.strong_id.model.Engine;
import org.sterl.jpa.strong_id.model.EngineId;
import org.sterl.jpa.strong_id.model.Vehicle;
import org.sterl.jpa.strong_id.model.Vin;
import org.sterl.jpa.strong_id.repository.EngineRepostory;
import org.sterl.jpa.strong_id.repository.VehicleRepository;

@DataJpaTest
class StrongCustomIdTest {

    @Autowired VehicleRepository vehicleRepository;
    @Autowired EngineRepostory engineRepostory;

    @Test
    void testSaveVehicle() {
        var e = vehicleRepository.save(new Vehicle(new Vin("123456"), "Fahrzeug1", null));
        System.out.println(e);
        
        System.out.println(vehicleRepository.findById(e.getVin()));
    }
    
    @Test
    void testSaveEngine() {
        var e = engineRepostory.save(new Engine(null, "Motor1"));
        System.out.println(e);
        
        System.out.println(engineRepostory.findById(new EngineId(e.getId())));
    }
    
    @Test
    void testSaveVehicleWithEngine() {
        var e = vehicleRepository.save(new Vehicle(new Vin("123459"), "Fahrzeug1", new Engine(null, "Motor1")));
        System.out.println(e);
        
        System.out.println(vehicleRepository.findById(e.getVin()));
    }

}
