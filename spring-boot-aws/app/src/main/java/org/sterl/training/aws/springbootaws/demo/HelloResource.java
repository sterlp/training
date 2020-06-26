package org.sterl.training.aws.springbootaws.demo;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloResource {

    @GetMapping("/")
    String getRoot() {
        return "Root: " + Instant.now().toString();
    }
    
    @GetMapping("/hello")
    String get() {
        return "Hello " + Instant.now().toString();
    }
}
