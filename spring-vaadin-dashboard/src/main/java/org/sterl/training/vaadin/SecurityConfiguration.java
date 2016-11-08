package org.sterl.training.vaadin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

import com.vaadin.server.VaadinSession;

/**
 * https://github.com/peholmst/SpringSecurityDemo
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
        auth.inMemoryAuthentication()
            .withUser("admin").password("admin").roles("ADMIN", "USER")
            .and()
            .withUser("user").password("user").roles("USER");
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }

    
    /**
     * A custom {@link SecurityContextHolderStrategy} that stores the {@link SecurityContext} in the Vaadin Session.
     */
    public static class VaadinSessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

        @Override
        public void clearContext() {
            getSession().setAttribute(SecurityContext.class, null);
        }

        @Override
        public SecurityContext getContext() {
            VaadinSession session = getSession();
            SecurityContext context = session.getAttribute(SecurityContext.class);
            if (context == null) {
                context = createEmptyContext();
                session.setAttribute(SecurityContext.class, context);
            }
            return context;
        }

        @Override
        public void setContext(SecurityContext context) {
            getSession().setAttribute(SecurityContext.class, context);
        }

        @Override
        public SecurityContext createEmptyContext() {
            return new SecurityContextImpl();
        }

        private static VaadinSession getSession() {
            VaadinSession session = VaadinSession.getCurrent();
            if (session == null) {
                throw new IllegalStateException("No VaadinSession bound to current thread");
            }
            return session;
        }
    }
}
