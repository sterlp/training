package org.sterl.training.vaadin.common.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.sterl.training.vaadin.common.event.ViewOpened;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Panel;

/**
 * Base panel fires an event as soon it is shown
 */
public abstract class BasePanelView extends Panel implements View {

    @Autowired protected ApplicationEventPublisher eventPublisher;
    private final String urlName;
    public BasePanelView(String urlName) {
        this.urlName = urlName;
    }
    @Override
    public void enter(ViewChangeEvent event) {
        eventPublisher.publishEvent(new ViewOpened(this));
    }
    
    public String getUrlName() {
        return urlName;
    }
}
