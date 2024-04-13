package com.smaugslair.thitracker.data.powers;

import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.util.AbilityModsRenderer;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class PowerSet implements Sheetable, Comparable<PowerSet> {


    @Id
    private String ssid;

    @Column(nullable = false)
    private String name;

    @Column()
    @Temporal(TemporalType.DATE)
    private Date updated;

    @Column(nullable = false, length = 2048)
    private String openText;

    @Column(nullable = false, length = 2048)
    private String abilityText;

    @Column(nullable = false, length = 5000)
    private String powersText;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "power_set_id" )
    @MapKey(name = "ability")
    Map<Ability, PowerSetMod> powerSetMods = new HashMap<>();

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


    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    public String getOpenText() {
        return openText;
    }

    public void setOpenText(String openText) {
        this.openText = openText;
    }

    public String getAbilityText() {
        return abilityText;
    }

    public void setAbilityText(String abilityText) {
        this.abilityText = abilityText;
    }

    public String getPowersText() {
        return powersText;
    }

    public void setPowersText(String powersText) {
        this.powersText = powersText;
    }

    private void createMod(Ability ability, Integer value) {
        if (value != null) {
            PowerSetMod mod = new PowerSetMod();
            mod.setPowerSet(this);
            mod.setAbility(ability);
            mod.setValue(value);
            powerSetMods.put(ability, mod);
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

    public Map<Ability, PowerSetMod> getPowerSetMods() {
        return powerSetMods;
    }

    public void setPowerSetMods(Map<Ability, PowerSetMod> powerSetMods) {
        this.powerSetMods = powerSetMods;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PowerSet.class.getSimpleName() + "[", "]")
                .add("ssid='" + ssid + "'")
                .add("name='" + name + "'")
                .add("updated=" + updated)
                .add("openText='" + openText + "'")
                .add("abilityText='" + abilityText + "'")
                .add("powersText='" + powersText + "'")
                .add("abilityMods='" + getAbilityModsText() + "'")
                .toString();
    }

    public boolean deepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerSet powerSet = (PowerSet) o;
        if( Objects.equals(ssid, powerSet.ssid)
                && Objects.equals(name, powerSet.name)
                && Objects.equals(updated, powerSet.updated)
                && Objects.equals(openText, powerSet.openText)
                && Objects.equals(abilityText, powerSet.abilityText)
                && Objects.equals(powersText, powerSet.powersText)) {
            //All else equal, compare mods
            if (powerSetMods.size() != powerSet.powerSetMods.size()) {
                return false;
            }
            for (PowerSetMod mod : powerSetMods.values()) {
                if (!powerSet.powerSetMods.containsValue(mod)) {
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
        PowerSet powerSet = (PowerSet) o;
        return ssid.equals(powerSet.ssid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssid);
    }

    @Override
    public int compareTo(PowerSet o) {
        return name.compareTo(o.getName());
    }

    @Transient
    private String abilityModsText;
    public String getAbilityModsText() {
        if (abilityModsText == null) {
            abilityModsText = AbilityModsRenderer.renderAmString(powerSetMods.values());
        }
        return abilityModsText;
    }

}
