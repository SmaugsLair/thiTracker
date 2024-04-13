package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class PowerDetails extends FormLayout {

    public PowerDetails(Power power, SortedSet<Power> allPowers) {

        addFormItem(new Span(power.getShortDescr()), "Short Description");
        addFormItem(new Span(power.getFullDescr()), "Full Description");
        if (!power.getAbilityMods().isEmpty()) {
            addFormItem(new Span(power.getAbilityMods()), "Ability Mods");
        }
        if (!power.getAssRules().isEmpty()) {
            addFormItem(new Span(power.getAssRules()), "Associated Rules");
        }
        if (!power.getPrerequisite().isEmpty()) {
            addFormItem(new Span(power.getPrerequisite()), "Prerequisites");
        }
        addFormItem(new Span(power.getPowerSets()), "Power Sets");
        if (!power.getSubPowers().isEmpty()) {
            addFormItem(new Span(power.getSubPowers()), "SubPowers");
            String[] strings = power.getSubPowers().split(":");
            List<String> subpowers = new ArrayList<>();
            for (Power p : allPowers) {
                if (p.getPowerTag().contains(strings[0])) {
                    subpowers.add(p.getName());
                }
            }
            addFormItem(new Span(subpowers.toString()), "SubPowers List");
        }

    }
}
