package org.sterl.jpa.embeddable.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>{@code 
 * create table person (id bigint not null, city varchar(255), 
 * street varchar(255), 
 * zip varchar(255), 
 * primary key (id))
 * }
 * </pre>
 */
@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Embedded
    private EmbeddedAddress address;
}