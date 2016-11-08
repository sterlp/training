package org.sterl.training.vaadin.ui.view.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.sterl.training.vaadin.service.auth.model.LoggedInUser;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SpringComponent
@UIScope
@SuppressWarnings("serial")
public final class DashboardMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";
    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private MenuItem settingsItem;
    
    private final CssLayout menuItemsLayout = new CssLayout();
    @Autowired private LoggedInUser user;
    @Autowired private List<ContentMenuItem> contentMenuItems = new ArrayList<>();

    public DashboardMenu() {
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();
    }

    @PostConstruct
    void init() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        setCompositionRoot(menuContent);
    }

    private Component buildTitle() {
        Label logo = new Label("QuickTickets <strong>Dashboard</strong>", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        settingsItem = settings.addItem("",
                new ThemeResource("img/profile-pic-300px.jpg"), null);
        /*
        settingsItem.addItem("Edit Profile", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                ProfilePreferencesWindow.open(user, false);
            }
        });
        settingsItem.addItem("Preferences", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                ProfilePreferencesWindow.open(user, true);
            }
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                DashboardEventBus.post(new UserLoggedOutEvent());
            }
        });
        */
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName()
                        .contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        menuItemsLayout.addStyleName("valo-menuitems");

        for (final ContentMenuItem view : contentMenuItems) {
            view.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(view);
                }
            });
            menuItemsLayout.addComponent(view);
        }
        return menuItemsLayout;

    }
    
    private void pageChange(ContentMenuItem menuItem) {
        final String STYLE_SELECTED = "selected";
        for (Iterator<Component> iterator = menuItemsLayout.iterator(); iterator.hasNext();) {
            Component contentMenuItem = iterator.next();
            if (menuItem.equals(contentMenuItem)) {
                contentMenuItem.addStyleName(STYLE_SELECTED);
            } else {
                contentMenuItem.removeStyleName(STYLE_SELECTED);
            }
        }
        UI.getCurrent().getNavigator().navigateTo(menuItem.getViewName());
    }

    @Override
    public void attach() {
        super.attach();
        //updateNotificationsCount(null);
        settingsItem.setText(user.getName());
    }
    /*
    @Subscribe
    public void postViewChange(final PostViewChangeEvent event) {
        // After a successful view change the menu can be hidden in mobile view.
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }

    @Subscribe
    public void updateNotificationsCount(
            final NotificationsCountUpdatedEvent event) {
        int unreadNotificationsCount = DashboardUI.getDataProvider()
                .getUnreadNotificationsCount();
        notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
        notificationsBadge.setVisible(unreadNotificationsCount > 0);
    }

    @Subscribe
    public void updateReportsCount(final ReportsCountUpdatedEvent event) {
        reportsBadge.setValue(String.valueOf(event.getCount()));
        reportsBadge.setVisible(event.getCount() > 0);
    }

    @Subscribe
    public void updateUserName(final ProfileUpdatedEvent event) {
        User user = getCurrentUser();
        settingsItem.setText(user.getFirstName() + " " + user.getLastName());
    }
    */
}
