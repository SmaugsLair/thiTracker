package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

public class TraitField implements TraitRow {

    private final Trait trait;
    private final String text;

    private final Component nameField;
    private final IntegerField pointField;
    //private final boolean editableLabel;
    private HorizontalLayout hl = null;

    public TraitField(Trait trait, CharacterEditor editor) {
        this(trait, true, editor);
    }

    public TraitField(Trait trait, boolean editableLabel, CharacterEditor editor) {
        this.text = trait.getName();
        this.trait = trait;
        if (editableLabel) {
            TextField textField = new TextField();
            textField.setValue(trait.getName());
            textField.addValueChangeListener(event -> {
                trait.setName(event.getValue());
                editor.updatePC();
            });
            nameField = textField;
        }
        else {
            nameField = new Span(trait.getName());
        }

        pointField = new IntegerField();
        pointField.setValue(trait.getPoints());
        pointField.setMin(0);
        if (TraitType.Hero.equals(trait.getType())) {
            pointField.setMax(1);
        }
        pointField.setStepButtonsVisible(true);
        pointField.setWidth("110px");
        pointField.addValueChangeListener(event -> {
            trait.setPoints(event.getValue());
            editor.updatePC();
        });

        if (trait.isDeletable()) {
            hl = new HorizontalLayout();
            hl.add(pointField);
            Button deleteButton = new UserSafeButton("X", event -> {
                editor.removeTrait(trait);
            });
            hl.add(deleteButton);
        }
    }

    @Override
    public Component getLeft() {
        return nameField;
    }

    @Override
    public Component getRight() {
        if (hl != null) {
            return hl;
        }
        return pointField;
    }

    @Override
    public String getColor() {
        return "";
    }

    @Override
    public String getLeftValue() {
        return text;
    }

    @Override
    public TraitType getTraitType() {
        return trait.getType();
    }
/*
    public void setReadOnly(boolean readOnly) {
        if (nameField instanceof TextField) {
            ((TextField)nameField).setReadOnly(readOnly);
        }
        pointField.setReadOnly(readOnly);
    }*/
}
