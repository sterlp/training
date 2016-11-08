package org.sterl.training.vaadin.ui.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class PageHeader extends HorizontalLayout {
    
    private final Label titleLabel;
    private HorizontalLayout toolbar;

    public PageHeader(String title) {
        addStyleName("viewheader");
        setSpacing(true);

        titleLabel = new Label(title);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        addComponent(titleLabel);
    }
    
    public HorizontalLayout addToolBar(Component... children) {
        toolbar = new HorizontalLayout(children);
        toolbar.setSpacing(true);
        toolbar.addStyleName("toolbar");
        this.addComponent(toolbar);
        return toolbar;
    }
}