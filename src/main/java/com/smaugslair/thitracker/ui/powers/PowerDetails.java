package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

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
