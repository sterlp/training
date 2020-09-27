package org.sterl.training.spring.jdbcsecurity.springjdbcsecurity;

import java.time.Clock;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleResource {

    @Autowired PasswordEncoder encoder;

    @RolesAllowed("ROLE_user")
    @GetMapping("/user")
    String get() {
        return "user: " + Clock.systemUTC().instant().toString();
    }
    
    @Secured("ROLE_admin")
    @GetMapping("/admin")
    String getAdmin() {
        return "admin: " + Clock.systemUTC().instant().toString();
    }
    
    @PostMapping("/hash")
    String hash(@RequestBody String password) {
        return this.encoder.encode(password);
    }
}
