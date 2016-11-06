package org.sterl.training.vaadin.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.sterl.training.vaadin.event.UserLoginRequestedEvent;
import org.sterl.training.vaadin.service.auth.activity.LoginBM;
import org.sterl.training.vaadin.service.auth.model.User;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;

import lombok.Getter;

/**
 * Class managing the session of the user and his state ...
 */
@SpringComponent
@VaadinSessionScope
public class UserSessionBM {

    @Autowired ApplicationEventPublisher eventPublisher;

    @Autowired LoginBM loginBM;
    @Getter
    private User user;
    
    public boolean login(String userName, String password) {
        try {
            user = loginBM.login(userName, password);
            eventPublisher.publishEvent(new UserLoginRequestedEvent(user));
            return true; 
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void updateUserName(String firstName) {
        this.user.setFirstName(firstName);
    }
    
    public boolean isLoggedIn() {
        return user != null;
    }
}
