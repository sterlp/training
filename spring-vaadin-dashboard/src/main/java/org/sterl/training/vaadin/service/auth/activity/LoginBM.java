package org.sterl.training.vaadin.service.auth.activity;

import org.springframework.stereotype.Service;
import org.sterl.training.vaadin.service.auth.model.User;

@Service
public class LoginBM {
    public User login(String userName, String password) throws IllegalArgumentException {
        // should throw exception on error
        if (password == null || password.length() == 0) throw new IllegalArgumentException("Unknown password");
        return new User(userName, null);
    }
}
