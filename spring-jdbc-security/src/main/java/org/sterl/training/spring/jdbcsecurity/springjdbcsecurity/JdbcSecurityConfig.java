package org.sterl.training.spring.jdbcsecurity.springjdbcsecurity;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.sterl.identitystore.api.IdentityStore;
import org.sterl.identitystore.api.VerificationResult;
import org.sterl.identitystore.api.VerificationResult.Status;
import org.sterl.identitystore.builder.IdentityStoreBuilder;

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

        final IdentityStore identityStore = IdentityStoreBuilder.jdbcBuilder(dataSource)
            .withPasswordQuery("select password from users where enabled = true AND username = ?")
            .withGroupsQuery("select authority from authorities where username = ?")
            .withGroupPrefix("ROLE_")
            .withCache(Duration.ofMinutes(15))
            .withCachedPassword(true)
            .build();

        auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
            
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authentication;
                final VerificationResult verificationResult = identityStore.verify(auth.getName(), 
                        auth.getCredentials() == null ? null : auth.getCredentials().toString());
                
                if (verificationResult.getStatus() == Status.VALID) {
                    final List<SimpleGrantedAuthority> authorities = verificationResult.getGroups().stream().map(g -> new SimpleGrantedAuthority(g)).collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(
                            authentication.getPrincipal(), 
                            authentication.getCredentials(), 
                            authorities);
                } else {
                    throw new BadCredentialsException("Wrong user name or password.");
                }
            }
        });

        /* spring build in JDBC identity store
        auth.jdbcAuthentication()
            .rolePrefix("ROLE_") // in spring a role needs this prefix
            .dataSource(this.dataSource);
            */
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // new SpringBCryptPbkdf2PasswordHash();
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
