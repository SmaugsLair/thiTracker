package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.TraitType;

public class TraitRowFilter {

    private boolean showHero = false;
    private boolean showDrama = false;

    public boolean isShowHero() {
        return showHero;
    }

    public void setShowHero(boolean showHero) {
        this.showHero = showHero;
    }

    public boolean isShowDrama() {
        return showDrama;
    }

    public void setShowDrama(boolean showDrama) {
        this.showDrama = showDrama;
    }

    public boolean test(TraitRow traitRow) {
        TraitType traitType = traitRow.getTraitType();
        if (traitType != null) {
            if (!showDrama && traitType.equals(TraitType.Drama)) {
                return false;
            }
            if (!showHero && traitType.equals(TraitType.Hero)) {
                return false;
            }
        }
        return true;
    }
}
