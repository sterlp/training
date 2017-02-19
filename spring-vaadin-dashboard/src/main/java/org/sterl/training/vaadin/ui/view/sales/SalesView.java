package org.sterl.training.vaadin.ui.view.sales;

import javax.annotation.PostConstruct;

import org.sterl.training.vaadin.ui.component.PageHeader;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = SalesView.NAME)
public class SalesView extends Panel implements View {
    public static final String NAME = "SalesView";
    
    private final VerticalLayout root = new VerticalLayout();

    @PostConstruct
    void init() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(new PageHeader("Sales"));
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}
