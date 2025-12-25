package org.sterl.training.axon.toy;

import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.training.axon.toy.command.NewToyCommand;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("toy")
public class ToyResource {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway; 
    
    
    @PostMapping
    CompletableFuture<String> create(String name) {
        return commandGateway.send(NewToyCommand.builder().name(name).build());
    }
}
