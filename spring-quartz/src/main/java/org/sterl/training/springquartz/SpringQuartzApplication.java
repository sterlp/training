package org.sterl.training.springquartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.sterl.training")
public class SpringQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringQuartzApplication.class, args);
    }

}
