package org.sterl.training.spring.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
            .antMatchers("/encrypt/**").authenticated()
            .antMatchers("/decrypt/**").authenticated()
        ;
    }
}
