package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.ui.games.CharacterDetails;

public class ProgressionPoint extends PointField {

    public ProgressionPoint(PlayerCharacter pc, CharacterDetails details) {
        setId("Progression Points");
        setValue(pc.getProgressionTokens());
        setMin(0);
        //setWidth("100px");
        setHasControls(true);
        addValueChangeListener(event -> {
            pc.setProgressionTokens(event.getValue());
            details.updatePC();
        });
    }

    @Override
    public String getPointName() {
        return "Progression Points";
    }
}
