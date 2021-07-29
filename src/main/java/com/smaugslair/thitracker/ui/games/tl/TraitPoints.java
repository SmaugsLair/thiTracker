package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.ui.games.CharacterDetails;
import com.vaadin.flow.component.textfield.IntegerField;

public class TraitPoints extends IntegerField {

    public TraitPoints(Trait trait, CharacterDetails details) {
        setValue(trait.getPoints());
        setMin(0);
        setHasControls(true);
        addValueChangeListener(event -> {
            trait.setPoints(event.getValue());
            details.updatePC();
        });
    }
}
