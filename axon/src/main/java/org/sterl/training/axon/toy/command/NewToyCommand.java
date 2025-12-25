package org.sterl.training.axon.toy.command;

import java.time.Instant;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(of = "id")
@Getter
public class NewToyCommand {

    @TargetAggregateIdentifier
    @Default
    private final String id = UUID.randomUUID().toString();
    private final String name;
    @Default
    private final Instant time = Instant.now();
}
