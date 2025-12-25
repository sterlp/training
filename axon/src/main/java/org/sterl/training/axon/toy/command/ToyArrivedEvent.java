package org.sterl.training.axon.toy.command;

import java.time.Instant;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Getter
public class ToyArrivedEvent {

    @TargetAggregateIdentifier
    private final String id;
    private final String name;
    private final Instant time;
}
