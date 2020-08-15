package org.sterl.training.spring.config.client;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
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
    
    @GetMapping("/all")
    public ResponseEntity<Map<?, ?>> getAll() {
        Properties props = new Properties();
        MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> props.setProperty(propName, env.getProperty(propName)));
        return ResponseEntity.ok(props);
    }
}

@Component
@Getter
@RefreshScope
class RefreshableConfig {
    @Value("${config.foo}")
    private String foo;
}
