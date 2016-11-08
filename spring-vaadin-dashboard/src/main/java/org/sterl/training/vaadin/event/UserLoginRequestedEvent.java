package org.sterl.training.vaadin.event;

import org.sterl.training.vaadin.service.auth.model.LoggedInUser;

import lombok.Data;

@Data
public class UserLoginRequestedEvent {
    private final LoggedInUser loggedInUser;
}
