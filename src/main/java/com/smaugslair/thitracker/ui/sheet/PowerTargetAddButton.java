package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.ui.components.UserSafeNativeButton;

public class PowerTargetAddButton extends UserSafeNativeButton {

    public PowerTargetAddButton(PowerTarget powerTarget, PowerSetEditor powerSetEditor) {
        setText("+");
        setEnabled(true);
        addClickListener(event -> {
            if (powerTarget.getPower().getSubPowers().isEmpty()) {
                powerSetEditor.addPowerTarget(powerTarget);
            }
            else {
                new SubPowerChoiceDialog(powerTarget.getPower(), powerSetEditor.getAllPowers()).open();
                //new ConfirmDialog("placeholder to select from subpowers").open();
            }
        });
    }
}
