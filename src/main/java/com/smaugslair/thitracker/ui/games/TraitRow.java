package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.ui.games.tl.PointField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;

public class TraitRow {

    private final String name;
    private final Component component;
    private final String color;

    public TraitRow(PointField pointField) {
        name = pointField.getPointName();
        component = (Component) pointField;
        color = "";
    }


    public TraitRow(String name, String color) {
        this.name = name;
        component = new Span();
        this.color = color;
    }

    public TraitRow(String name) {
        this.name = name;
        component = new Span();
        color = "w3-light-grey";
    }

    public String getName() {
        return name;
    }

    public Component getComponent() {
        return component;
    }

    public String getColor() {
        return color;
    }
}
