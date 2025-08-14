package com.smaugslair.thitracker.rules;


public enum Ability {

    Perception(0, 0),
    Stealth(0,1),
    Aim(1,0),
    Dodge(1,1),
    Strength(2,0),
    Toughness(2,1),
    Influence(3, 0),
    SelfControl(3,1, "Self-Control"),
    Initiative(4,0),
    Movement(4, 1),
    Choice(-1),
    TravelMult(-1); //Not used, preserved for data integrity

    private final int x;
    private final int y;
    private final String displayName;
    private final int baseValue = 3;

    private static Ability abilityDim[][] = new Ability[5][2];

    static {
        for (Ability ability : values()) {
            if (ability.isMapped()) {
                abilityDim[ability.x][ability.y] = ability;
            }
        }
    }

    public boolean isMapped() {
        return x >=0;
    }

    Ability(int z) {
        this(z,z);
    }


    Ability(int i, int j) {
        this(i, j, null);
    }

    Ability(int i, int j, String s) {
        x = i;
        y = j;
        displayName = s;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDisplayName() {
        if (displayName == null) {
            return name();
        }
        return displayName;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public static Ability getAt(int i, int j) {
        return abilityDim[i][j];
    }

    public String getRendered(int value) {
        return getDisplayName() + " " + value;
    }
}
