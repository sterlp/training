package org.sterl.training.vaadin.common.event;

import org.sterl.training.vaadin.common.view.BasePanelView;

import lombok.Data;

@Data
public class ViewOpened {

    private final BasePanelView panelView;
}
