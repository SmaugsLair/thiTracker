package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.data.selection.MultiSelectionEvent;
import com.vaadin.flow.data.selection.MultiSelectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class SubPowerChoiceDialog extends ConfirmDialog implements MultiSelectionListener<CheckboxGroup<Power>, Power> {

    private final Integer choices;
    //private final UserSafeButton saveButton = new UserSafeButton("Save");
    private CheckboxGroup<Power> checkboxGroup;

    public SubPowerChoiceDialog(Power power, SortedSet<Power> allPowers, List<Power> herosPowers, Button saveButton) {
        setConfirmButton(saveButton);
        saveButton.setEnabled(false);

        String[] strings = power.getSubPowers().split(":");
        String tag = strings[0];
        choices = Integer.valueOf(strings[1]);

        setHeaderTitle("Choose "+choices+" from "+tag+ " subpowers");

        Button tagBrowser = new UserSafeButton("Subpower details", event -> {
            getUI().ifPresent(ui -> {
                ui.getPage().open("powerbrowser/"+tag, tag);
            });
        });
        add(tagBrowser);
        List<Power> subpowers = new ArrayList<>();
        for (Power p : allPowers) {
            if (p.getPowerTag().contains(tag)) {
                subpowers.add(p);
            }
        }
        checkboxGroup = new CheckboxGroup<>("Subpowers", subpowers);
        checkboxGroup.setItemLabelGenerator(Power::getName);
        checkboxGroup.addSelectionListener(this);
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        checkboxGroup.setItemEnabledProvider(power1 -> {
           return power1.prerequsitesMet(herosPowers.stream().map(Power::getName).collect(Collectors.toList()));
        });
        add(checkboxGroup);
    }

    @Override
    public void selectionChange(MultiSelectionEvent<CheckboxGroup<Power>, Power> multiSelectionEvent) {
        enableConfirmButton(isValid());
        //saveButton.setEnabled();
    }

    public Set<Power> getSelectedPowers() {
        return checkboxGroup.getSelectedItems();
    }

    public boolean isValid () {
        return checkboxGroup.getSelectedItems().size() == choices;
    }
}
