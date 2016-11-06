package org.sterl.training.vaadin.ui.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.sterl.training.vaadin.ui.view.menu.DashboardMenu;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import lombok.Getter;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SpringComponent
@UIScope
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    @Autowired private DashboardMenu menu;
    @Getter private final ComponentContainer content = new CssLayout();
    
    @PostConstruct
    void init() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(menu);
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }
}
