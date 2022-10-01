package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.HeroPowerSet;
import com.smaugslair.thitracker.data.powers.Power;

public class PowerTarget implements Comparable<PowerTarget> {

    private final Power power;
    private final boolean available;
    private final int taken;
    private final HeroPowerSet heroPowerSet;

    public PowerTarget(Power power, boolean available, int taken, HeroPowerSet heroPowerSet) {
        this.power = power;
        this.available = available;
        this.taken = taken;
        this.heroPowerSet = heroPowerSet;
    }

    public Power getPower() {
        return power;
    }

    public boolean isAvailable() {
        return available;
    }

    public HeroPowerSet getHeroPowerSet() {
        return heroPowerSet;
    }

    public String getName() {
        return power.getName();
    }

    public String getShortDescr() {
        return power.getShortDescr();
    }

    public String getPrerequisite() {
        return power.getPrerequisite();
    }

    public String getMaxTaken() {
        return power.getMaxTaken();
    }
    public int getTimesTaken() {
        return taken;
    }

    public String getTakenText() {
        return taken + "/"+getMaxTaken();
    }

    public String getFullDescr() {
        return power.getFullDescr();
    }

    @Override
    public int compareTo(PowerTarget o) {
        return power.compareTo(o.power);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PowerTarget{");
        sb.append("power=").append(power);
        sb.append(", available=").append(available);
        sb.append(", taken=").append(taken);
        sb.append(", heroPowerSet=").append(heroPowerSet);
        sb.append('}');
        return sb.toString();
    }
}
