package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.TraitType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;

public class MetaRow implements TraitRow {

    private final Span label = new Span();
    private final Component component;
    private final String color;
    private TraitType traitType = null;

    public MetaRow(String text) {
        label.setText(text);
        component = new Span();
        color = "light-grey";
    }
    public MetaRow(Button button, TraitType traitType) {
        label.setText("");
        component = button;
        color = "";
        this.traitType = traitType;
    }

    @Override
    public Component getLeft() {
        return label;
    }

    @Override
    public Component getRight() {
        return component;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getLeftValue() {
        return label.getText();
    }

    @Override
    public TraitType getTraitType() {
        return traitType;
    }
}
