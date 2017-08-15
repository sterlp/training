package org.sterl.training.vaadin.service.auth.model;

import java.io.Serializable;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;

@SpringComponent
@VaadinSessionScope
public class LoggedInUser implements Serializable {
    private String name;
    private boolean loggedIn = false;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    @Override
    public String toString() {
        return "LoggedInUser:loggedIn --> " + loggedIn;
    }
}
