package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.TraitType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;

public class PlayerRow implements TraitRow {

    private final Span label = new Span();
    private final Button button = new Button("Enable edits");

    @Override
    public Component getLabel() {
        return null;
    }

    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public String getLabelValue() {
        return null;
    }

    @Override
    public TraitType getTraitType() {
        return null;
    }
}
