package org.sterl.jpa.embeddable.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class EmbeddedAddress {

    private String street;
    private String city;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="value",
                           column=@Column(name="zip")
        )
    })
    private EmbeddedZip zip;
}
