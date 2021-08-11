package com.smaugslair.thitracker.ui.games;

public class AbilityRowFilter {

    private boolean showRows = false;

    public boolean isShowRows() {
        return showRows;
    }

    public void setShowRows(boolean showRows) {
        this.showRows = showRows;
    }

    public boolean test(AbilityRow abilityRow) {
        if (!showRows && !abilityRow.isHeader()) {
            return false;
        }
        return true;
    }
}
