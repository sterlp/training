package org.sterl.training.vaadin.common.event;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import lombok.Data;

@Data
public class ViewOpened {

    private final ViewChangeEvent changeEvent;
    
    /**
     * Name of the view being displayed
     */
    public String getViewName() {
        return changeEvent.getViewName();
    }
}
