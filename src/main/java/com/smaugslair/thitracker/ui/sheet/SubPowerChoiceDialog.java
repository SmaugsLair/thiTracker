package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.selection.MultiSelectionEvent;
import com.vaadin.flow.data.selection.MultiSelectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class SubPowerChoiceDialog extends Dialog implements MultiSelectionListener<CheckboxGroup<Power>, Power> {

    private final Integer choices;
    private final UserSafeButton saveButton = new UserSafeButton("Save");

    public SubPowerChoiceDialog(Power power, SortedSet<Power> allPowers) {
        String[] strings = power.getSubPowers().split(":");
        String tag = strings[0];
        choices = Integer.valueOf(strings[1]);

        add("Choose "+choices+" from "+tag);

        List<Power> subpowers = new ArrayList<>();
        for (Power p : allPowers) {
            if (p.getPowerTag().contains(tag)) {
                subpowers.add(p);
            }
        }
        CheckboxGroup<Power> checkboxGroup = new CheckboxGroup<>("Subpowers", subpowers);
        checkboxGroup.setItemLabelGenerator(Power::getName);
        checkboxGroup.addSelectionListener(this);
        add(checkboxGroup);
        saveButton.setEnabled(false);
        add(saveButton);
    }

    @Override
    public void selectionChange(MultiSelectionEvent<CheckboxGroup<Power>, Power> multiSelectionEvent) {
        saveButton.setEnabled(choices == multiSelectionEvent.getAllSelectedItems().size());
    }
}
