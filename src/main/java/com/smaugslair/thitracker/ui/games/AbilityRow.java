package com.smaugslair.thitracker.ui.games;

import com.vaadin.flow.component.Component;

public class AbilityRow {

    private final TraitRow row1, row2;
    private final boolean header;

    public AbilityRow() {
        header = true;
        row1 = new TraitRow("Abilities");
        row2 = new TraitRow("");
    }

    public AbilityRow(TraitRow row1, TraitRow row2) {
        header = false;
        this.row1 = row1;
        this.row2 = row2;
    }

    public String getName1() {
        return row1.getName();
    }
    public String getName2() {
        return row2.getName();
    }

    public Component getComponent1() {
        return row1.getComponent();
    }
    public Component getComponent2() {
        return row2.getComponent();
    }

    public boolean isHeader() {
        return header;
    }

    public String getColor() {
        return row1.getColor();
    }
}
