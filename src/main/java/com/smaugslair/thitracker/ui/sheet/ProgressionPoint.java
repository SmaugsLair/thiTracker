package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.IntegerField;

public class ProgressionPoint implements TraitRow {

    private final static String labelStr = "Progression Points";

    private final Component label = new Span(labelStr);
    private final IntegerField pointField = new IntegerField();

    public ProgressionPoint(PlayerCharacter pc, CharacterSheet sheet) {
        pointField.setValue(pc.getProgressionTokens());
        pointField.setMin(0);
        pointField.setStepButtonsVisible(true);
        pointField. addValueChangeListener(event -> {
            pc.setProgressionTokens(event.getValue());
            sheet.updatePC();
        });
    }


    @Override
    public Component getLabel() {
        return label;
    }

    @Override
    public Component getComponent() {
        return pointField;
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
