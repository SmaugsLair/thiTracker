package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.powers.Mod;

import java.util.Collection;

public class AbilityModsRenderer {

    public static String renderAmString(Collection<? extends Mod> mods) {
        StringBuilder sb = new StringBuilder();
        for (Mod mod : mods) {
            sb.append(mod.getAbility().getRendered(mod.getValue())).append("  ");

        }
        return sb.toString();
    }
}
