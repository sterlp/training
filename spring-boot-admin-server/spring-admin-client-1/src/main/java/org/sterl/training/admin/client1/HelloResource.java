package org.sterl.training.admin.client1;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloResource {

    @GetMapping("/")
    String time() {
        return Instant.now().toString();
    }
}
