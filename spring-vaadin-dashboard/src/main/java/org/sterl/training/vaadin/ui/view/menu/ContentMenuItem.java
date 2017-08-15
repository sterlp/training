package org.sterl.training.vaadin.ui.view.menu;

import com.vaadin.ui.Button;

public abstract class ContentMenuItem extends Button {
    public abstract String getViewName();
    
    public ContentMenuItem() {
        setPrimaryStyleName("valo-menu-item");
    }
}
