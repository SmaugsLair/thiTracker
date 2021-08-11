package com.smaugslair.thitracker.ui.games.tl;

import com.vaadin.flow.component.textfield.NumberField;

public class DerivedDouble extends NumberField implements PointField {

    private final String name;

    public DerivedDouble(String name, Double value) {
        this.name = name;
        setValue(value);
        setReadOnly(true);
        setWidth("40px");
    }

    public String getPointName() {
        return name;
    }
}
