package org.sterl.training.spring.config.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class SpringConfigClientApplication {

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("trust-store.jks").getFile().getAbsolutePath()); 
        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        SpringApplication.run(SpringConfigClientApplication.class, args);
    }
}
