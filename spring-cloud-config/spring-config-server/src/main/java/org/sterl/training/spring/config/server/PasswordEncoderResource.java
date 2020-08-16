package org.sterl.training.spring.config.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordEncoderResource {

    @Autowired PasswordEncoder encoder;

    @PostMapping("/encode")
    public String encodePassword(@RequestBody(required = true) String password) {
        return encoder.encode(password);
    }
}
