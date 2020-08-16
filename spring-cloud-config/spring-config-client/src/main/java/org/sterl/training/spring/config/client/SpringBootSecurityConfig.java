package org.sterl.training.spring.config.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@EnableWebSecurity
@Configuration
public class SpringBootSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${clients}") String clients;
    @Autowired ObjectMapper objectMapper;
    
    final TypeReference<HashMap<String,UserData>> TYPE = new TypeReference<>() {};
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final HashMap<String, UserData> users = objectMapper.readValue(clients, TYPE);
        final InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authz = auth.inMemoryAuthentication();
        
        users.entrySet().forEach(e -> authz.withUser(e.getKey()).password(e.getValue().getPassword()).roles(e.getValue().arrayRoles()));
    }
    
    /** TODO update users, e.g. as soon the refresh event occurs
    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh(RefreshScopeRefreshedEvent event) {
    }
    */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .httpBasic()
         .and()
            .authorizeRequests()
            .antMatchers("/**").authenticated()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Data
    static class UserData {
        private static final String[] EMPTY = {};
        private String password;
        private Set<String> roles = new HashSet<>();
        
        String[] arrayRoles() {
            if (roles == null || roles.isEmpty()) return EMPTY;
            else return roles.toArray(new String[roles.size()]);
        }
    }
}
