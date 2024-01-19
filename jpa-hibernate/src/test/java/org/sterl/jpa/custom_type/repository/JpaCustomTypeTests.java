package org.sterl.jpa.custom_type.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sterl.jpa.custom_type.model.Address;
import org.sterl.jpa.custom_type.model.Zip;

@SpringBootTest
class JpaCustomTypeTests {

    @Autowired
    private AddressRepository subject;
    
    @BeforeEach
    void beforeEach() {
        subject.deleteAllInBatch();
    }

    @Test
    void testFindByZip() {
        // GIVEN
        var address1 = subject.save(Address.newAddress("Foo Street 1", "80000", "München"));
        subject.save(Address.newAddress("Foo Street 2", "80001", "München"));
        assertThat(subject.count()).isEqualTo(2);
        
        // WHEN
        assertThat(subject.findByZip(address1.getZip())).singleElement();
        List<Address> result = subject.findByZip(address1.getZip());

        // THEN
        assertThat(result).singleElement();
        assertThat(result.get(0).getZip()).isEqualTo(address1.getZip());
    }
    

    @Test
    void testSearch() {
        // GIVEN
        subject.save(Address.newAddress("Foo Street 1", "80000", "München 1"));
        subject.save(Address.newAddress("Foo Street 2", "80001", "München 2"));
        subject.save(Address.newAddress("Foo Street 2", "60000", "Foo"));

        // WHEN
        List<Address> result = subject.search("8000%");

        // THEN
        assertThat(result).hasSize(2);
    }
}