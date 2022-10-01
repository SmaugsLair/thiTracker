package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.AbilityScore;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbilityRow {

    private final Logger log = LoggerFactory.getLogger(AbilityRow.class);

    private final String label1, label2;
    private final Component component1, component2;
    private final boolean header;
    private final String color;

    public AbilityRow() {
        header = true;
        color = "light-grey";
        label1 = "Abilities";
        label2 = "";
        component1 = new Span();
        component2 = new Span();
    }

    public AbilityRow(AbilityScore abilityScore1, AbilityScore abilityScore2, CharacterSheet sheet) {

        //log.info("creating AbilityRow with " +abilityScore1+" and "+abilityScore2);

        header = false;
        color = "";
        if (abilityScore1 == null) {
            label1 = "";
            component1 = new Span();
        }
        else {
            label1 = abilityScore1.getAbility().getDisplayName();
            component1 = new AbilityField(abilityScore1, sheet).getPointField();
        }
        label2 = abilityScore2.getAbility().getDisplayName();
        component2 = new AbilityField(abilityScore2, sheet).getPointField();
    }

    public AbilityRow(DerivedField field) {
        header = false;
        color = "";
        label1 = "";
        component1 = new Span();
        label2 = field.getName();
        component2 = field.getSpan();
    }

    public String getLabel1() {
        return label1;
    }

    public String getLabel2() {
        return label2;
    }

    public Component getComponent1() {
        return component1;
    }

    public Component getComponent2() {
        return component2;
    }

    public boolean isHeader() {
        return header;
    }

    public String getColor() {
        return color;
    }
}
