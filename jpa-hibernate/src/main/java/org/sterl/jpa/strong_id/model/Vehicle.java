package org.sterl.jpa.strong_id.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name="value",
                           column=@Column(name="vin")
        )
    })
    private Vin vin;
    
    private String name;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Engine engine;
}
