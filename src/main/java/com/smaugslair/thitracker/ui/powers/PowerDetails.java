package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.ui.components.FormattedTextBlock;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class PowerDetails extends VerticalLayout {

    public PowerDetails(Power power, SortedSet<Power> allPowers) {

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        horizontalLayout.setWidthFull();
        //setResponsiveSteps(new FormLayout.ResponsiveStep("0", 4));
        horizontalLayout.add(new FormattedTextBlock(power.getShortDescr(), "Short Description"));
        horizontalLayout.add(new FormattedTextBlock(power.getAbilityMods(), "Ability Mods"));
        horizontalLayout.add(new FormattedTextBlock(power.getAssRules(), "Associated Rules"));
        horizontalLayout.add(new FormattedTextBlock(power.getPrerequisite(), "Prerequisites"));
        add(horizontalLayout);

        /*addFormItem(new Span(power.getShortDescr()), "Short Description");

        if (!power.getAbilityMods().isEmpty()) {
            addFormItem(new Span(power.getAbilityMods()), "Ability Mods");
        }
        if (!power.getAssRules().isEmpty()) {
            addFormItem(new Span(power.getAssRules()), "Associated Rules");
        }
        if (!power.getPrerequisite().isEmpty()) {
            addFormItem(new Span(power.getPrerequisite()), "Prerequisites");
        }*/
        add(new FormattedTextBlock(power.getFullDescr(), "Full Description"));
        /*addFormItem(formattedTextBlock, "Full Description");
        setColspan(formattedTextBlock, 4);*/

        String powerSets = power.getPowerSets();
        if (powerSets == null) {
            powerSets = "";
        }

        add(new FormattedTextBlock(powerSets, "Power Sets"));
        if (!power.getSubPowers().isEmpty()) {
            add(new FormattedTextBlock(power.getSubPowers(), "SubPowers"));
            String[] strings = power.getSubPowers().split(":");
            List<String> subpowers = new ArrayList<>();
            for (Power p : allPowers) {
                if (p.getPowerTag().contains(strings[0])) {
                    subpowers.add(p.getName());
                }
            }
            add(new FormattedTextBlock(subpowers.toString(), "SubPowers List"));
        }

    }
}
