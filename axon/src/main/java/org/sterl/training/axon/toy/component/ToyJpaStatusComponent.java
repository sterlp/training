package org.sterl.training.axon.toy.component;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.sterl.training.axon.toy.aggregate.Toy.ToyState;
import org.sterl.training.axon.toy.command.ToyArrivedEvent;
import org.sterl.training.axon.toy.model.ToyEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ToyJpaStatusComponent {

    private final ToyRepository repository;

    @EventHandler
    @Transactional
    public void on(ToyArrivedEvent event) { 
        var e = new ToyEntity(event.getId(), event.getName(), event.getTime(), ToyState.AVAILABLE); 
        repository.save(e);
        log.info("Toy={} saved", event.getId());
    }
}
