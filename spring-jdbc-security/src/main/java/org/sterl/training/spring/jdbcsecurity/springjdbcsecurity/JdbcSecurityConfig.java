package org.sterl.training.spring.jdbcsecurity.springjdbcsecurity;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.sterl.spring.hash.SpringBCryptPbkdf2PasswordHash;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, 
        securedEnabled = true, 
        jsr250Enabled = true)
public class JdbcSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private DataSource dataSource;
     
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
      throws Exception {
        auth.jdbcAuthentication()
            .rolePrefix("ROLE_") // in spring a role needs this prefix
            .dataSource(this.dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SpringBCryptPbkdf2PasswordHash();
    }

    
    @Override
    protected void configure(HttpSecurity httpSecurity)
      throws Exception {
        httpSecurity
            .authorizeRequests().antMatchers("/hash").permitAll()
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .formLogin().and().httpBasic();

        httpSecurity
            .csrf().ignoringAntMatchers("/hash")
            .and()
            .headers().frameOptions().sameOrigin();
    }
    
}
