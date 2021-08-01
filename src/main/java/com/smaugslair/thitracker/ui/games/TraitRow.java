package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.ui.games.tl.PointField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;

public class TraitRow {

    private final String name;
    private final Component component;

    public TraitRow(PointField pointField) {
        name = pointField.getPointName();
        component = pointField;
    }

    public TraitRow(String name) {
        this.name = name;
        component = new Span();
    }

    public String getName() {
        return name;
    }

    public Component getComponent() {
        return component;
    }
}
