package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.ui.components.ValidTextField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class TopRow implements TraitRow {

    private ValidTextField pcName = new ValidTextField();
    private final Span userName;
    private final String color;

    public TopRow(String userName, String color, CharacterSheet sheet) {

        pcName.addValidator(new StringLengthValidator("2 char min", 2, 40));

        pcName.setValue(sheet.getPC().getName());
        pcName.addValueChangeListener(valueChangeEvent -> {
            if (pcName.isValid()) {
                sheet.getPC().setName(pcName.getValue());
                sheet.updatePC();
            }
        });

        this.userName = new Span(userName);
        this.color = color;
    }

    @Override
    public Component getLeft() {
        return pcName;
    }

    @Override
    public Component getRight() {
        return userName;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getLeftValue() {
        return "";
    }

    @Override
    public TraitType getTraitType() {
        return null;
    }
}
