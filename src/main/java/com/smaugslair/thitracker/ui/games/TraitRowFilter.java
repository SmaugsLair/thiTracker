package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.ui.games.tl.TraitPoint;

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
        if (traitRow.getComponent() instanceof TraitPoint) {
            Trait trait = ((TraitPoint)traitRow.getComponent()).getTrait();
            if (!showDrama && trait.getType().equals(TraitType.Drama)) {
                return false;
            }
            if (!showHero && trait.getType().equals(TraitType.Hero)) {
                return false;
            }
        }
        return true;
    }
}
