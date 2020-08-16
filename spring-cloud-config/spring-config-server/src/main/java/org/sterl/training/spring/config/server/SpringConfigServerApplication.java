package org.sterl.training.spring.config.server;

import java.security.SecureRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigServer
@SpringBootApplication
public class SpringConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfigServerApplication.class, args);
    }
}

@EnableWebSecurity
@Configuration
class SecuritySettings extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .requiresChannel().anyRequest().requiresSecure() // only SSL allowed, now optional, only for older spring versions needed
         .and()
            .csrf()
            .disable()
            .httpBasic()
         .and()
            .authorizeRequests()
            .antMatchers("/**").authenticated()
        ;
    }
    
    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(BCryptVersion.$2A, 10, new SecureRandom());
    }
}
