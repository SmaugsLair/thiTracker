package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.HeroPower;
import com.smaugslair.thitracker.data.pc.HeroSubPower;
import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.components.UserSafeNativeButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PowerTargetAddButton extends UserSafeNativeButton {

    private final Logger log = LoggerFactory.getLogger(PowerTargetAddButton.class);
    public PowerTargetAddButton(PowerTarget powerTarget, PowerSetEditor powerSetEditor) {
        setText("+");
        setEnabled(true);
        addClickListener(event -> {
            if (powerTarget.getPower().getSubPowers().isEmpty()) {
                powerSetEditor.addPowerTarget(powerTarget, null);
            }
            else {
                UserSafeButton saveButton = new UserSafeButton("Save");

                List<Power> powerList = new ArrayList<>();
                for (HeroPower heroPower : powerSetEditor.getHeroPowers()) {
                    powerList.add(heroPower.getPower());
                    for (HeroSubPower subPower : heroPower.getSubPowers()) {
                        powerList.add(subPower.getPower());
                    }
                }

                SubPowerChoiceDialog subPowerChoiceDialog = new SubPowerChoiceDialog(
                        powerTarget.getPower(), powerSetEditor.getAllPowers(), powerList, saveButton);
                subPowerChoiceDialog.open();

                saveButton.addClickListener(buttonClickEvent -> {
                    if (subPowerChoiceDialog.isValid()) {
                        powerSetEditor.addPowerTarget(powerTarget, subPowerChoiceDialog.getSelectedPowers());
                    }
                    subPowerChoiceDialog.close();
                });
                //new ConfirmDialog("placeholder to select from subpowers").open();
            }
        });
    }

}
