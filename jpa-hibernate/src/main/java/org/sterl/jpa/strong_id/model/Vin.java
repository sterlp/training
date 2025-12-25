package org.sterl.jpa.strong_id.model;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
public class Vin implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vin;
    
    public void setVin(Long value) {
        System.err.println("set vin " + value);
        this.vin = value;
    }
}
