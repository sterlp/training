package org.sterl.training.vaadin.ui.view;

import org.springframework.context.ApplicationEventPublisher;
import org.sterl.training.vaadin.common.event.ViewOpened;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

public class EventAwareNavigator extends Navigator {

    private final ApplicationEventPublisher eventPublisher;
    public EventAwareNavigator(UI ui, ComponentContainer container, ApplicationEventPublisher eventPublisher) {
        super(ui, container);
        this.eventPublisher = eventPublisher;
        
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                // Since there's no conditions in switching between the views
                // we can always return true.
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                eventPublisher.publishEvent(new ViewOpened(event));
            }
        });
    }
}
