package org.sterl.training.axon.toy;

import java.time.Instant;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sterl.training.axon.toy.aggregate.Toy;
import org.sterl.training.axon.toy.command.NewToyCommand;
import org.sterl.training.axon.toy.command.ToyArrivedEvent;

class ToyTest {

    private AggregateTestFixture<Toy> fixture; 

    @BeforeEach 
    void setUp() {
        fixture = new AggregateTestFixture<>(Toy.class); 
    }
    @Test
    void test() {
        var time = Instant.now();
        fixture.givenNoPriorActivity()  
            .when(NewToyCommand.builder().id("1").name("foo").time(time).build()) 
            .expectEvents(new ToyArrivedEvent("1", "foo", time));
    }

}
