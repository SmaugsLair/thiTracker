package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.rules.Ability;
import org.apache.commons.beanutils.BeanUtils;

public class AbilityModsRenderer {

    public static String renderAmString(Object owner, Integer amChoice) {
        StringBuilder sb = new StringBuilder();
        for (Ability ability : Ability.values()) {
            try {
                String prop = BeanUtils.getProperty(owner, "am" + ability.name());
                if (prop != null) {
                    sb.append(ability.getRendered(Integer.parseInt(prop))).append("  ");
                }
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (amChoice != null) {
            sb.append("Choice ").append(amChoice);
        }
        return sb.toString();
    }
}
