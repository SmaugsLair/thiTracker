package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;

public class PowerDetails extends FormLayout {

    public PowerDetails(Power power) {

        addFormItem(new Span(power.getShortDescr()), "Short Description");
        addFormItem(new Span(power.getFullDescr()), "Full Description");
        addFormItem(new Span(power.getAbilityMods()), "Ability Mods");
        addFormItem(new Span(power.getAssRules()), "Associated Rules");
        addFormItem(new Span(power.getPrereq()), "Prerequisites");
        addFormItem(new Span(power.getPowerSets()), "Power Sets");
        addFormItem(new Span(power.getSubPowers()), "SubPowers");

    }
}
