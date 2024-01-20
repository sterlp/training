package org.sterl.jpa.embeddable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sterl.jpa.embeddable.model.EmbeddedAddress;
import org.sterl.jpa.embeddable.model.EmbeddedZip;
import org.sterl.jpa.embeddable.model.Person;
import org.sterl.jpa.embeddable.repository.PersonRepository;

@SpringBootTest
class EmbeddableTest {

    @Autowired
    private PersonRepository subject;
    
    @Test
    void testSearchEmbeddedTypeValue() {
        // GIVEN
        var person1 = new Person(null,
            new EmbeddedAddress(
                    "Straße 1",
                    "Berlin",
                    new EmbeddedZip("10000")
                    )
        );
        var person2 = new Person(null,
            new EmbeddedAddress(
                    "Straße 2",
                    "Fooo",
                    new EmbeddedZip("bar")
                    )
        );
        subject.save(person1);
        subject.save(person2);
        
        // WHEN
        var result = subject.findByZip("100%");
        
        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAddress().getZip()).isEqualTo(new EmbeddedZip("10000"));
     }

}
