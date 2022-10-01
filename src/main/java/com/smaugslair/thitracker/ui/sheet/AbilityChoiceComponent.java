package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.rules.Ability;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

public class AbilityChoiceComponent extends HorizontalLayout {


    private final Ability ability;
    private IntegerField integerField = new IntegerField();

    public AbilityChoiceComponent(Ability ability) {
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.ability = ability;
        add(new Label(ability.getDisplayName()), integerField);
        integerField.setMin(0);
        integerField.setHasControls(true);
        integerField.setValue(0);
    }

    public Ability getAbility() {
        return ability;
    }

    public Integer getValue() {
        return integerField.getValue();
    }
}
