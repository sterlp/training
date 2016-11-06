package org.sterl.training.vaadin.ui.view.dashboard;

import javax.annotation.PostConstruct;

import org.sterl.training.vaadin.ui.view.menu.ContentMenuItem;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DashboardMenuItem extends ContentMenuItem {
    @PostConstruct
    void init() {
        setIcon(FontAwesome.HOME);
        setCaption("Dashboard");
    }
    @Override
    public String getViewName() {
        return DashboardView.NAME;
    }
}
