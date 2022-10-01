package com.smaugslair.thitracker.data.powers;

import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.util.AbilityModsRenderer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.*;

@Entity
public class  Power implements Sheetable, Comparable<Power> {

    private static final Logger log = LoggerFactory.getLogger(Power.class);

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

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "power_id" )
    @MapKey(name = "ability")
    Map<Ability, PowerMod> powerMods = new HashMap<>();

    @Column(nullable = false)
    private Boolean metaPower = false;

    @Column(length = 1000)
    private String subPowers;

    private String powerSets;


    @Transient
    private Map<String, Integer> powerSetMap = new HashMap<>();

    @Transient
    private boolean badPrerequisite = false;

    @Transient
    private boolean hasPrerequisite = false;

    @Transient
    private List<String> orPrereqs, andPrereqs;


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

    public boolean isBadPrerequisite() {
        return badPrerequisite;
    }

    public void setBadPrerequisite(boolean badPrerequisite) {
        this.badPrerequisite = badPrerequisite;
    }

    public boolean isHasPrerequisite() {
        return hasPrerequisite;
    }

    public void setHasPrerequisite(boolean hasPrerequisite) {
        this.hasPrerequisite = hasPrerequisite;
    }

    public void parsePrerequisite(List<String> powerNames) {
        if (prerequisite.isEmpty() || prerequisite.toLowerCase().startsWith("none")) {
            hasPrerequisite = false;
            badPrerequisite = false;
            return;
        }
        hasPrerequisite = true;
        if (prerequisite.contains("OR")) {
            orPrereqs = Arrays.asList(prerequisite.split("OR"));
            orPrereqs.replaceAll(String::trim);
            log.info("OR prereqs: "+ orPrereqs + " for " + getName());
            if (!powerNames.containsAll(orPrereqs)) {
                badPrerequisite = true;
            }
        }
        else if (prerequisite.contains("AND")) {
            andPrereqs =Arrays.asList(prerequisite.split("AND"));
            andPrereqs.replaceAll(String::trim);
            log.info("AND prereqs: "+ andPrereqs+ " for " + getName());

            if (!powerNames.containsAll(andPrereqs)) {
                badPrerequisite = true;
            }
        }
        else if (!powerNames.contains(prerequisite)) {
            badPrerequisite = true;
        }
    }

    public boolean prerequsitesMet(List<String> powerNames) {
        if (!hasPrerequisite) {
            return true;
        }
        if (badPrerequisite) {
            return false;
        }
        //ORs == at least one
        if (orPrereqs != null) {
            for (String powerName : orPrereqs) {
                if (powerNames.contains(powerName)) {
                    return true;
                }
            }
            return false;
        }
        //ANDS == all
        if (andPrereqs != null) {
            return powerNames.containsAll(andPrereqs);
        }
        //Single power
        return powerNames.contains(prerequisite);
    }

    private void createMod(Ability ability, Integer value) {
        if (value != null && value != 0) {
            PowerMod powerMod = new PowerMod();
            powerMod.setPower(this);
            powerMod.setAbility(ability);
            powerMod.setValue(value);
            powerMods.put(ability, powerMod);
        }
    }

    public void setAmPerception(Integer value) {
        createMod(Ability.Perception, value);
    }

    public void setAmStealth(Integer value) {
        createMod(Ability.Stealth, value);
    }

    public void setAmAim(Integer value) {
        createMod(Ability.Aim, value);
    }

    public void setAmDodge(Integer value) {
        createMod(Ability.Dodge, value);
    }

    public void setAmStrength(Integer value) {
        createMod(Ability.Strength, value);
    }

    public void setAmToughness(Integer value) {
        createMod(Ability.Toughness, value);
    }

    public void setAmInfluence(Integer value) {
        createMod(Ability.Influence, value);
    }

    public void setAmSelfControl(Integer value) {
        createMod(Ability.SelfControl, value);
    }

    public void setAmInitiative(Integer value) {
        createMod(Ability.Initiative, value);
    }

    public void setAmMovement(Integer value) {
        createMod(Ability.Movement, value);
    }

    public void setAmTravelMult(Integer value) {
        createMod(Ability.TravelMult, value);
    }

    public void setAmChoice(Integer value) {
        createMod(Ability.Choice, value);
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
        if ( Objects.equals(ssid, power.ssid)
                && Objects.equals(name, power.name)
                && Objects.equals(shortDescr, power.shortDescr)
                && Objects.equals(fullDescr, power.fullDescr)
                && Objects.equals(powerTag, power.powerTag)
                && Objects.equals(assRules, power.assRules)
                && Objects.equals(maxTaken, power.maxTaken)
                && Objects.equals(prerequisite, power.prerequisite)
                && Objects.equals(metaPower, power.metaPower)
                && Objects.equals(subPowers, power.subPowers)
                && Objects.equals(powerSets, power.powerSets)) {
            //All else equal, compare mods
            if (powerMods.size() != power.powerMods.size()) {
                return false;
            }
            for (PowerMod powerMod : powerMods.values()) {
                if (!power.powerMods.containsValue(powerMod)) {
                    return false;
                }
            }
            return true;

        }
        return false;

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
            //abilityMods  = "TODO";
            abilityMods = AbilityModsRenderer.renderAmString(powerMods.values());
        }
        return abilityMods;
    }

    public Map<Ability, PowerMod> getPowerMods() {
        return powerMods;
    }

    public void setPowerMods(Map<Ability, PowerMod> powerMods) {
        this.powerMods = powerMods;
    }
}
