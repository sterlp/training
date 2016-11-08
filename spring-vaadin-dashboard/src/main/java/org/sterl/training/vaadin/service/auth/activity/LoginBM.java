package org.sterl.training.vaadin.service.auth.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.sterl.training.vaadin.event.UserLoginRequestedEvent;
import org.sterl.training.vaadin.service.auth.model.LoggedInUser;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.UI;

@Service
public class LoginBM {
    @Autowired private ApplicationContext context;
    @Autowired private ApplicationEventPublisher eventPublisher;
    @Autowired private AuthenticationManager authenticationManager;

    public boolean login(String userName, String password) throws IllegalArgumentException {
        try {
            Authentication token = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
            // used WEBSOCKET_XHR and skipped this step completely.
            // UI.getCurrent().getPushConfiguration().setTransport(Transport.WEBSOCKET);
            UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            
            LoggedInUser loggedInUser = context.getBean(LoggedInUser.class);
            loggedInUser.setLoggedIn(true);
            loggedInUser.setName(userName);
            eventPublisher.publishEvent(new UserLoginRequestedEvent(loggedInUser));
            
            return true;
        } catch (AuthenticationException ex) {
            UI.getCurrent().getPushConfiguration().setPushMode(PushMode.DISABLED);
            VaadinSession session = VaadinSession.getCurrent();
            if (session != null) {
                System.err.println("Clear user session ...");
                session.setAttribute(LoggedInUser.class, null);
            }
            SecurityContextHolder.getContext().setAuthentication(null);
            return false;
        }
    }
}
