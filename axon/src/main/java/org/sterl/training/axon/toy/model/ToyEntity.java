package org.sterl.training.axon.toy.model;

import java.time.Instant;

import org.sterl.training.axon.toy.aggregate.Toy.ToyState;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "toy")
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class ToyEntity {

    @Id
    private String id;
    private String name;
    private Instant receivedDate;
    @Enumerated(EnumType.STRING)
    private ToyState state;
}
