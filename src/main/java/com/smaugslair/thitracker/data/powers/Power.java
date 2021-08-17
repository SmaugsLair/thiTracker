package com.smaugslair.thitracker.data.powers;

import com.smaugslair.thitracker.util.AbilityModsRenderer;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

@Entity
public class Power implements Sheetable, Comparable<Power> {

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

    private String prerequisite;

    private String amPerception;
    private String amStealth;
    private String amAim;
    private String amDodge;
    private Integer amStrength;
    private Integer amToughness;
    private String amInfluence;
    private String amSelfControl;
    private String amInitiative;
    private String amMovement;
    private String amTravelMult;

    private Integer amChoice;

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

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getAmPerception() {
        return amPerception;
    }

    public void setAmPerception(String amPerception) {
        this.amPerception = amPerception;
    }

    public String getAmStealth() {
        return amStealth;
    }

    public void setAmStealth(String amStealth) {
        this.amStealth = amStealth;
    }

    public String getAmAim() {
        return amAim;
    }

    public void setAmAim(String amAim) {
        this.amAim = amAim;
    }

    public String getAmDodge() {
        return amDodge;
    }

    public void setAmDodge(String amDodge) {
        this.amDodge = amDodge;
    }

    public Integer getAmStrength() {
        return amStrength;
    }

    public void setAmStrength(Integer amStrength) {
        this.amStrength = amStrength;
    }

    public Integer getAmToughness() {
        return amToughness;
    }

    public void setAmToughness(Integer amToughness) {
        this.amToughness = amToughness;
    }

    public String getAmInfluence() {
        return amInfluence;
    }

    public void setAmInfluence(String amInfluence) {
        this.amInfluence = amInfluence;
    }

    public String getAmSelfControl() {
        return amSelfControl;
    }

    public void setAmSelfControl(String amSelfControl) {
        this.amSelfControl = amSelfControl;
    }

    public String getAmInitiative() {
        return amInitiative;
    }

    public void setAmInitiative(String amInitiative) {
        this.amInitiative = amInitiative;
    }

    public String getAmMovement() {
        return amMovement;
    }

    public void setAmMovement(String amMovement) {
        this.amMovement = amMovement;
    }

    public String getAmTravelMult() {
        return amTravelMult;
    }

    public void setAmTravelMult(String amTravelMult) {
        this.amTravelMult = amTravelMult;
    }

    public Integer getAmChoice() {
        return amChoice;
    }

    public void setAmChoice(Integer amChoice) {
        this.amChoice = amChoice;
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
        StringTokenizer st = new StringTokenizer(powerSets, ",");
        while (st.hasMoreTokens()) {
            StringTokenizer ist = new StringTokenizer(st.nextToken(), ":");
            powerSetMap.put(ist.nextToken().trim(), Integer.parseInt(ist.nextToken()));
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


    public boolean deepEquals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Power power = (Power) o;
        return Objects.equals(ssid, power.ssid)
                && Objects.equals(name, power.name)
                && Objects.equals(shortDescr, power.shortDescr)
                && Objects.equals(fullDescr, power.fullDescr)
                && Objects.equals(powerTag, power.powerTag)
                && Objects.equals(assRules, power.assRules)
                && Objects.equals(maxTaken, power.maxTaken)
                && Objects.equals(prerequisite, power.prerequisite)
                && Objects.equals(amPerception, power.amPerception)
                && Objects.equals(amStealth, power.amStealth)
                && Objects.equals(amAim, power.amAim)
                && Objects.equals(amDodge, power.amDodge)
                && Objects.equals(amInfluence, power.amInfluence)
                && Objects.equals(amSelfControl, power.amSelfControl)
                && Objects.equals(amInitiative, power.amInitiative)
                && Objects.equals(amMovement, power.amMovement)
                && Objects.equals(amTravelMult, power.amTravelMult)
                && Objects.equals(amChoice, power.amChoice)
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

    @Override
    public int compareTo(Power o) {
        return name.compareTo(o.name);
    }


    @Transient
    private String abilityMods;
    public String getAbilityMods() {
        if (abilityMods == null) {
            abilityMods = AbilityModsRenderer.renderAmString(this, amChoice);
        }
        return abilityMods;
    }
}
