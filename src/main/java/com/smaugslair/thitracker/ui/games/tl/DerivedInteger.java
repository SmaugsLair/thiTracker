package com.smaugslair.thitracker.ui.games.tl;

import com.vaadin.flow.component.textfield.IntegerField;

public class DerivedInteger extends IntegerField implements PointField {

    private final String name;

    public DerivedInteger(String name, Integer value) {
        this.name = name;
        setValue(value);
        setReadOnly(true);
        setWidth("40px");
    }

    public String getPointName() {
        return name;
    }
}
