package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.ui.components.UserSafeNativeButton;

public class PowerTargetAddButton extends UserSafeNativeButton {

    public PowerTargetAddButton(PowerTarget powerTarget, PowerSetEditor powerSetEditor) {
        setText("+");
        setEnabled(true);
        addClickListener(event -> {
            powerSetEditor.addPowerTarget(powerTarget);
        });
    }
}
