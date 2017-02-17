package org.sterl.training.vaadin.ui.view.dashboard;

import javax.annotation.PostConstruct;

import org.sterl.training.vaadin.common.view.BasePanelView;
import org.sterl.training.vaadin.ui.component.PageHeader;

import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = DashboardView.NAME)
public class DashboardView extends BasePanelView {
    public static final String NAME = "DashboardView";

    private final VerticalLayout root = new VerticalLayout();

    public DashboardView() {
        super(DashboardView.NAME);
    }


    @PostConstruct
    void init() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(new PageHeader("Dashboard"));
    }
}
