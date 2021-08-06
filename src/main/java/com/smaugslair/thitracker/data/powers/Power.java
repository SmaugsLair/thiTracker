package com.smaugslair.thitracker.data.powers;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.*;

@Entity
public class Power implements Sheetable {

    @Id
    private String ssid;

    @Column(nullable = false)
    private String name;

    private String shortDescr;

    @Column(length = 5000)
    private String fullDescr;

    private String powerTag;

    private String assRules;

    @Column(nullable = false)
    private String maxTaken;

    private String prereq;

    private String abilityMods = "";

    @Column(nullable = false)
    private Boolean metaPower = false;

    @Column(length = 1000)
    private String subPowers;

    private String powerSets;


    @Transient
    private Map<String, Integer> powerSetMap = new HashMap<>();


    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescr() {
        return shortDescr;
    }

    public void setShortDescr(String shortDescr) {
        this.shortDescr = shortDescr;
    }

    public String getFullDescr() {
        return fullDescr;
    }

    public void setFullDescr(String fullDescr) {
        this.fullDescr = fullDescr;
    }

    public String getPowerTag() {
        return powerTag;
    }

    public void setPowerTag(String powerTag) {
        this.powerTag = powerTag;
    }

    public String getAssRules() {
        return assRules;
    }

    public void setAssRules(String assRules) {
        this.assRules = assRules;
    }

    public String getMaxTaken() {
        return maxTaken;
    }

    public void setMaxTaken(String makTaken) {
        this.maxTaken = makTaken;
    }

    public String getPrereq() {
        return prereq;
    }

    public void setPrereq(String prereq) {
        this.prereq = prereq;
    }

    public String getAbilityMods() {
        return abilityMods;
    }

    public void setAbilityMods(String abilityMods) {
        this.abilityMods = abilityMods;
    }

    public Boolean getMetaPower() {
        return metaPower;
    }

    public void setMetaPower(Boolean metaPower) {
        this.metaPower = metaPower;
    }

    public String getSubPowers() {
        return subPowers;
    }

    public void setSubPowers(String subPowers) {
        this.subPowers = subPowers;
    }

    public String getPowerSets() {
        return powerSets;
    }

    public void setPowerSets(String powerSets) {
        this.powerSets = powerSets;
    }

    public Map<String, Integer> getPowerSetMap() {
        return powerSetMap;
    }

    public void fillPowerSetMap() {
        if (powerSets == null) return;
        if (powerSets.endsWith("|")) {
            StringTokenizer st = new StringTokenizer(powerSets, "|");
            while (st.hasMoreTokens()) {
                powerSetMap.put(st.nextToken(), Integer.parseInt(st.nextToken()));
            }
        }
        else {
            StringTokenizer st = new StringTokenizer(powerSets, ",");
            while (st.hasMoreTokens()) {
                StringTokenizer ist = new StringTokenizer(st.nextToken(), ":");
                powerSetMap.put(ist.nextToken(), Integer.parseInt(ist.nextToken()));
            }
        }
    }

    public void addToPowerSets(String name, Integer tier) {
        powerSetMap.put(name, tier);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry: powerSetMap.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
        }
        powerSets = StringUtils.removeEnd(sb.toString(), ", ");
    }

    @Transient Integer tier;
    public Integer getTier() {
        if (tier == null) {
            int working = 11;
            for (Integer i: powerSetMap.values()) {
                working = Math.min(working, i);
            }
            tier = working;
        }
        return tier;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Power.class.getSimpleName() + "[", "]")
                .add("ssid='" + ssid + "'")
                .add("name='" + name + "'")
                .add("shortDescr='" + shortDescr + "'")
                .add("fullDescr='" + fullDescr + "'")
                .add("powerTag='" + powerTag + "'")
                .add("assRules='" + assRules + "'")
                .add("makTaken='" + maxTaken + "'")
                .add("prereq='" + prereq + "'")
                .add("abilityMods='" + abilityMods + "'")
                .add("metaPower=" + metaPower)
                .add("subPowers='" + subPowers + "'")
                .add("powerSetMap='"+ powerSetMap + "'")
                .toString();
    }

    public boolean deepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Power power = (Power) o;
        return Objects.equals(ssid, power.ssid)
                && Objects.equals(name, power.name)
                && Objects.equals(shortDescr, power.shortDescr)
                && Objects.equals(fullDescr, power.fullDescr)
                && Objects.equals(powerTag, power.powerTag)
                && Objects.equals(assRules, power.assRules)
                && Objects.equals(maxTaken, power.maxTaken)
                && Objects.equals(prereq, power.prereq)
                && Objects.equals(abilityMods, power.abilityMods)
                && Objects.equals(metaPower, power.metaPower)
                && Objects.equals(subPowers, power.subPowers)
                && Objects.equals(powerSets, power.powerSets);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Power power = (Power) o;
        return ssid.equals(power.ssid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssid);
    }
}
