package org.sterl.training.spring.config.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;

@RestController
@RequestMapping("/")
public class ExampleResource {

    @Autowired
    RefreshableConfig config;
    
    @Autowired
    ConfigurableEnvironment env;
    
    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok(config.getFoo());
    }
}

@Component
@Getter
@RefreshScope
class RefreshableConfig {
    @Value("${config.foo}")
    private String foo;
}
