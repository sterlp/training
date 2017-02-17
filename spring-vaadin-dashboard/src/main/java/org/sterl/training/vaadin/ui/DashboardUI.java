package org.sterl.training.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.sterl.training.vaadin.event.UserLoginRequestedEvent;
import org.sterl.training.vaadin.service.auth.model.LoggedInUser;
import org.sterl.training.vaadin.ui.view.LoginView;
import org.sterl.training.vaadin.ui.view.MainView;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

//@Widgetset("DashboardWidgetSet")
@Push(transport = Transport.WEBSOCKET_XHR)
@SpringUI
@Theme("dashboard") // you can delete src/main/resources/VAADIN and replace this with valo default style
@Title("Vaadin 8 Spring Boot")
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
            
            // navigate to first view if needed
            if (getPage().getUriFragment() != null && getPage().getUriFragment().startsWith("!")) {
                navigator.navigateTo(getPage().getUriFragment().substring(1));
            }

            // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
            // used WEBSOCKET_XHR and skipped this step completely.
            // UI.getCurrent().getPushConfiguration().setTransport(Transport.WEBSOCKET);
            UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
        } else {
            UI.getCurrent().getPushConfiguration().setPushMode(PushMode.DISABLED);
            setContent(loginView);
        }
    }
    
    @EventListener
    public void onLogin(UserLoginRequestedEvent login) {
        System.err.println("onLogin -> " + login);
        updateContent(login.getLoggedInUser());
    }
}
