package org.sterl.training.axon.toy.aggregate;

import java.time.Instant;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.sterl.training.axon.toy.command.NewToyCommand;
import org.sterl.training.axon.toy.command.ToyArrivedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@Getter
@NoArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode(of = "id")
public class Toy {

    @AggregateIdentifier 
    private String id;
    private String name;
    private Instant arrived;
    private ToyState state;
    
    public enum ToyState {
        AVAILABLE,
        RESERVED,
        SOLD
    }
    
    @CommandHandler 
    public Toy(NewToyCommand command) { 
        if (command.getTime().toEpochMilli() % 5 ==0) {
            throw new IllegalStateException("No worker available to receive the new toy - try again later.");
        }

        AggregateLifecycle.apply(new ToyArrivedEvent(command.getId(), command.getName(), command.getTime())); 
    }
    
    @EventSourcingHandler 
    protected void handle(ToyArrivedEvent event) { 
        this.id = event.getId();
        this.state = ToyState.AVAILABLE;
        this.name = event.getName();
        this.arrived = event.getTime();
        
        log.info("New toy available {}", this);
    }
}
