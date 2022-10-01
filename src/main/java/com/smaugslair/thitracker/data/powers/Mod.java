package com.smaugslair.thitracker.data.powers;

import com.smaugslair.thitracker.rules.Ability;

public interface Mod {
    Ability getAbility();

    Integer getValue();
}
