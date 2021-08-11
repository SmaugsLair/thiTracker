package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.ui.games.tl.PointField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;

public class DerivedInteger extends IntegerField implements PointField {

    private final String name;

    public DerivedInteger(String name, Integer value) {
        this.name = name;
        setValue(value);
        setReadOnly(true);
        setWidth("50px");
    }

    public String getPointName() {
        return name;
    }
}
