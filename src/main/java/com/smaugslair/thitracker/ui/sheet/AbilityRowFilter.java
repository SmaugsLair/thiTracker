package com.smaugslair.thitracker.ui.sheet;

public class AbilityRowFilter {

    private boolean showRows = false;

    public boolean isShowRows() {
        return showRows;
    }

    public void setShowRows(boolean showRows) {
        this.showRows = showRows;
    }

    public boolean test(AbilityRow abilityRow) {
        return showRows || abilityRow.isHeader();
    }
}
