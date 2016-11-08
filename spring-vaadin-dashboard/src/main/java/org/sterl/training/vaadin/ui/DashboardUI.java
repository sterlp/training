package org.sterl.training.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.sterl.training.vaadin.event.UserLoginRequestedEvent;
import org.sterl.training.vaadin.service.auth.model.LoggedInUser;
import org.sterl.training.vaadin.ui.view.LoginView;
import org.sterl.training.vaadin.ui.view.MainView;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

//@Widgetset("DashboardWidgetSet")
@Push(transport = Transport.WEBSOCKET_XHR)
@SpringUI
@Theme("dashboard")
public class DashboardUI extends UI {

    @Autowired private MainView mainView;
    @Autowired private LoginView loginView;
    @Autowired private SpringViewProvider viewProvider;
    @Autowired private LoggedInUser user;

    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent(user);
    }
    
    private void updateContent(LoggedInUser user) {
        System.err.println("onLogin -> " + user);
        if (user != null && user.isLoggedIn()) {
            setContent(mainView);
            Navigator navigator = new Navigator(this, mainView.getContent());
            navigator.addProvider(viewProvider);
            navigator.addView("", new Navigator.EmptyView());
        } else {
            setContent(loginView);
        }
    }
    
    @EventListener
    public void onLogin(UserLoginRequestedEvent login) {
        System.err.println("onLogin -> " + login);
        updateContent(login.getLoggedInUser());
    }
}
