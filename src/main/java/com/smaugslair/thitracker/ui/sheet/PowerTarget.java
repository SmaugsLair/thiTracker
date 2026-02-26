package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.HeroPowerSet;
import com.smaugslair.thitracker.data.powers.Power;

public class PowerTarget implements Comparable<PowerTarget> {

    private final Power power;
    private final Integer tier;
    private final boolean available;
    private final int taken;
    private final HeroPowerSet heroPowerSet;

    public PowerTarget(Power power, Integer tier, boolean available, int taken, HeroPowerSet heroPowerSet) {
        this.power = power;
        this.tier = tier;
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

    public Integer getMaxTaken() {
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
        String sb = "PowerTarget{" + "power=" + power +
                ", available=" + available +
                ", taken=" + taken +
                ", heroPowerSet=" + heroPowerSet +
                '}';
        return sb;
    }

    public Integer getTier() {
        return tier;
    }
}
