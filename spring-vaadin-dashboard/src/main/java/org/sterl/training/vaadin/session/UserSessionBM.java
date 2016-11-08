package org.sterl.training.vaadin.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sterl.training.vaadin.service.auth.activity.LoginBM;

/**
 * Class managing the session of the user and his state ...
 */
@Service
public class UserSessionBM {

    @Autowired private LoginBM loginBM;

    public boolean login(String userName, String password) {
        return loginBM.login(userName, password);
    }
}
