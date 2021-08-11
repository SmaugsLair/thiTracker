package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.ui.games.CharacterDetails;
import com.vaadin.flow.component.textfield.IntegerField;

public class TraitPoint extends IntegerField implements PointField {

    private final Trait trait;

    public TraitPoint(Trait trait, CharacterDetails details) {
        this.trait = trait;
        setValue(trait.getPoints());
        setMin(0);
        //setWidth("100px");
        setHasControls(true);
        addValueChangeListener(event -> {
            trait.setPoints(event.getValue());
            details.updatePC();
        });
    }

    public Trait getTrait() {
        return trait;
    }

    @Override
    public String getPointName() {
        return trait.getName();
    }
}
