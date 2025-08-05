package com.smaugslair.thitracker.ui.components.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class HeroCountEvent extends ComponentEvent<Component> {
    public HeroCountEvent(Component source, boolean fromClient) {
        super(source, fromClient);
    }
}
