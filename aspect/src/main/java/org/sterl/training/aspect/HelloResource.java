package org.sterl.training.aspect;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @MethodAnnotation
    @GetMapping("/method")
    String method() {
        return "Hello: " + Instant.now();
    }
    
    @MethodAnnotation("MY_NAME")
    @GetMapping("/custom")
    String customName() {
        return "Hello: " + Instant.now();
    }
}
