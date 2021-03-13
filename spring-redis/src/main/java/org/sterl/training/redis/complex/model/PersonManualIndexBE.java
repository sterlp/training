package org.sterl.training.redis.complex.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("personmi")
@Data @AllArgsConstructor @NoArgsConstructor
public class PersonManualIndexBE {
    
    @Id
    private String name;
    private Set<String> rooms = new HashSet<>();
    
    public PersonManualIndexBE(String name) {
        super();
        this.name = name;
    }
    
    public PersonManualIndexBE addRoom(String name) {
        rooms.add(name);
        return this;
    }
}
