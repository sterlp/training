package org.sterl.training.vaadin.ui.component;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class PageHeader extends HorizontalLayout {
    
    private final Label titleLabel;

    public PageHeader(String title) {
        addStyleName("viewheader");
        setSpacing(true);

        titleLabel = new Label(title);
        //titleLabel.setId("dashboard-title");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        addComponent(titleLabel);
    }
}