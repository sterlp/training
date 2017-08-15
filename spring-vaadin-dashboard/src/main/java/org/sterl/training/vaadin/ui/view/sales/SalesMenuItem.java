package org.sterl.training.vaadin.ui.view.sales;

import javax.annotation.PostConstruct;

import org.springframework.core.annotation.Order;
import org.sterl.training.vaadin.ui.view.menu.ContentMenuItem;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@Order(2)
@SpringComponent
@UIScope
public class SalesMenuItem extends ContentMenuItem {
    @PostConstruct
    void init() {
        setIcon(FontAwesome.TABLE);
        setCaption("Sales");
    }
    @Override
    public String getViewName() {
        return SalesView.NAME;
    }
}
