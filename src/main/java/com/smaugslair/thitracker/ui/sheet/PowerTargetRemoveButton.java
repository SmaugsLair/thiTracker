package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.ui.components.UserSafeNativeButton;

import java.util.List;

public class PowerTargetRemoveButton extends UserSafeNativeButton {

    public PowerTargetRemoveButton(PowerTarget powerTarget, PowerSetEditor powerSetEditor) {

        List<String> prereqList = powerSetEditor.getSatisfiedPrereqs();
        if (prereqList.contains(powerTarget.getPower().getName())) {
            setEnabled(false);
            setText("@");
            return;
        }
        setText("-");
        setEnabled(true);
        addClickListener(event -> {
            powerSetEditor.removePowerTarget(powerTarget);
        });
    }
}
