package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.ui.components.UserSafeNativeButton;

public class PowerTargetRemoveButton extends UserSafeNativeButton {

    public PowerTargetRemoveButton(PowerTarget powerTarget, PowerSetEditor powerSetEditor) {
        setText("-");
        setEnabled(true);
        addClickListener(event -> {
            powerSetEditor.removePowerTarget(powerTarget);
        });
    }
}
