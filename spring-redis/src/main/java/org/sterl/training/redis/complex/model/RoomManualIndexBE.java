package org.sterl.training.redis.complex.model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("roomi")
@Data @NoArgsConstructor
public class RoomManualIndexBE {

    @Id
    private String name;
    private Set<String> persons = new LinkedHashSet<>();
    
    public RoomManualIndexBE addPerson(String name) {
        this.persons.add(name);
        return this;
    }

    public RoomManualIndexBE(String name) {
        super();
        this.name = name;
    }
}
