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
    TravelMult(5,1, "Travel Mult", true, 2);

    private final int x;
    private final int y;
    private final String displayName;
    private final boolean multiplier;
    private final int baseValue;

    private static Ability abilityDim[][] = new Ability[6][2];

    static {
        for (Ability ability : values()) {
            abilityDim[ability.x][ability.y] = ability;
        }
    }


    Ability(int i, int j) {
        this(i, j, null);
    }

    Ability(int i, int j, String s) {
        this(i, j, s, false, 3);
    }

    Ability(int i, int j, String s, boolean m, int b) {
        x = i;
        y = j;
        displayName = null;
        multiplier = m;
        baseValue = b;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ability{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", multiplier=").append(multiplier);
        sb.append(", baseValue=").append(baseValue);
        sb.append('}');
        return sb.toString();
    }

    public String getRendered(int value) {
        return getDisplayName() + " " + (multiplier?"*":(value<1?"":"+")) + value;
    }
}
