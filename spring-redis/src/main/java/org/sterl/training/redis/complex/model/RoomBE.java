package org.sterl.training.redis.complex.model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("r")
@Data @NoArgsConstructor
public class RoomBE {

    @Id
    private String name;
    private Set<PersonBE> persons = new LinkedHashSet<>();
    
    
    
    public RoomBE addPerson(String name) {
        this.persons.add(new PersonBE(name));
        return this;
    }

    public RoomBE(String name) {
        super();
        this.name = name;
    }
}
