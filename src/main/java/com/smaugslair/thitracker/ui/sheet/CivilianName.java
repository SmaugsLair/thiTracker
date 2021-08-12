package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;

public class CivilianName implements TraitRow {

    private final static String labelStr = "Civilian ID";

    private final Component label = new Span(labelStr);
    private final TextField field = new TextField();

    public CivilianName(PlayerCharacter pc, CharacterSheet sheet) {
        field.setValue(pc.getCivilianId() == null ? "" : pc.getCivilianId());
        field.setPlaceholder("civilian id");
        field.addValueChangeListener(event -> {
            pc.setCivilianId(event.getValue());
            sheet.updatePC();
        });
    }


    @Override
    public Component getLabel() {
        return label;
    }

    @Override
    public Component getComponent() {
        return field;
    }

    @Override
    public String getColor() {
        return "";
    }

    @Override
    public String getLabelValue() {
        return labelStr;
    }

    @Override
    public TraitType getTraitType() {
        return null;
    }
}
