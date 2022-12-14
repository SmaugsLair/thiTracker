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

    public MetaRow(String heroName, String displayName, String color) {
        label.setText(heroName);
        component = new Span(displayName);
        this.color = color;
    }

    @Override
    public Component getLabel() {
        return label;
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getLabelValue() {
        return label.getText();
    }

    @Override
    public TraitType getTraitType() {
        return traitType;
    }
}
