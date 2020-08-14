package org.sterl.training.spring.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ExampleResource {

    @Value("${config.foo}")
    private String bar;
    
    @GetMapping
    public String get() {
        return bar;
    }
}
