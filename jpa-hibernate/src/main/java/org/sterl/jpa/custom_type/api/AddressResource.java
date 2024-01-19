package org.sterl.jpa.custom_type.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.jpa.custom_type.model.Address;
import org.sterl.jpa.custom_type.repository.AddressRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("address")
public class AddressResource {

    private final AddressRepository addressRepository;
    
    @PostConstruct
    void init() {
        addressRepository.save(Address.newAddress("Foo", "80000", "München"));
    }
    @GetMapping
    public List<Address> list() {
        return addressRepository.findAll();
    }
}