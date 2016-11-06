package org.sterl.training.vaadin.event;

import org.sterl.training.vaadin.service.auth.model.User;

import lombok.Data;
import lombok.ToString;

@Data
public class UserLoginRequestedEvent {
    private final User user;
}
