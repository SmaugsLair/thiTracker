package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.AbilityScore;
import com.vaadin.flow.component.textfield.TextField;

public class AbilityField {

    private final AbilityScore abilityScore;
    private final TextField pointField = new TextField();

    public AbilityField(AbilityScore abilityScore, CharacterSheet details) {
        this.abilityScore = abilityScore;
        pointField.setValue(String.valueOf(abilityScore.getPoints()));
        pointField.setReadOnly(true);
        //pointField.setMin(0);
        //pointField.setWidth("40px");
        //setHasControls(true);
        /*pointField.addValueChangeListener(event -> {
            abilityScore.setPoints(event.getValue());
            details.updatePC();
        });*/
    }

    public String getLabel() {
        return abilityScore.getAbility().getDisplayName();
    }

    public TextField getPointField() {
        return pointField;
    }

}
