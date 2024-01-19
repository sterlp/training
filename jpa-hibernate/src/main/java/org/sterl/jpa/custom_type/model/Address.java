package org.sterl.jpa.custom_type.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private String street;
    private Zip zip;
    private String city;
    
    
    public static Address newAddress(String street, String zip, String city) {
        return Address.builder()
                .street(street)
                .zip(new Zip(zip))
                .city(city)
                .build();
    }
}
