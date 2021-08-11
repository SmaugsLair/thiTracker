package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.pc.AbilityScore;
import com.smaugslair.thitracker.ui.games.CharacterDetails;
import com.vaadin.flow.component.textfield.IntegerField;

public class AbilityPoint extends IntegerField implements PointField {

    private final AbilityScore abilityScore;

    public AbilityPoint(AbilityScore abilityScore, CharacterDetails details) {
        this.abilityScore = abilityScore;
        setValue(abilityScore.getPoints());
        setMin(0);
        setWidth("40px");
        //setHasControls(true);
        addValueChangeListener(event -> {
            abilityScore.setPoints(event.getValue());
            details.updatePC();
        });
    }

    @Override
    public String getPointName() {
        return abilityScore.getName();
    }
}
