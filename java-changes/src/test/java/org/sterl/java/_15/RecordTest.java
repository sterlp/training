package org.sterl.java._15;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

/**
 * https://openjdk.java.net/jeps/359
 */
class RecordTest {
    
    @Data
    public final class Car14 {
        @NotNull
        private final String name;
        private final int hp;
        public Car14(String name, int hp) {
            super();
            this.name = name;
            this.hp = hp;
            if (this.hp < 100) {
                throw new IllegalArgumentException("HP has to be bigger than 100.");
            }
        }
    }
    
    // Java 15 record preview
    @SuppressWarnings("preview")
    public record Car15(@NotNull String name, int hp) {
        public Car15 {
            if (hp < 100) {
                throw new IllegalArgumentException("HP has to be bigger than 100.");
            }
        }
    }

    @Test
    void test() {
        System.out.println(new Car14("Car 1", 150));
        System.out.println(new Car15("Car 2", 150));
    }
    
    @Test
    void testValidation() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            final Validator validator = factory.getValidator();
            
            System.out.println(validator.validate(new Car14(null, 200)));
            System.out.println(validator.validate(new Car15(null, 200)));
        }  
    }
    
    @Test
    void testJackson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(new Car15("F1", 100)));
    }
    

}
