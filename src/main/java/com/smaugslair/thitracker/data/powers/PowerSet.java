package com.smaugslair.thitracker.data.powers;

import com.smaugslair.thitracker.util.AbilityModsRenderer;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

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

    private Integer amPerception;
    private Integer amStealth;
    private Integer amAim;
    private Integer amDodge;
    private Integer amStrength;
    private Integer amToughness;
    private Integer amInfluence;
    private Integer amSelfControl;
    private Integer amInitiative;
    private Integer amMovement;
    private Integer amTravelMult;
    private Integer amChoice;

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

    public Integer getAmPerception() {
        return amPerception;
    }

    public void setAmPerception(Integer amPerception) {
        this.amPerception = amPerception;
    }

    public Integer getAmStealth() {
        return amStealth;
    }

    public void setAmStealth(Integer amStealth) {
        this.amStealth = amStealth;
    }

    public Integer getAmAim() {
        return amAim;
    }

    public void setAmAim(Integer amAim) {
        this.amAim = amAim;
    }

    public Integer getAmDodge() {
        return amDodge;
    }

    public void setAmDodge(Integer amDodge) {
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

    public Integer getAmInfluence() {
        return amInfluence;
    }

    public void setAmInfluence(Integer amInfluence) {
        this.amInfluence = amInfluence;
    }

    public Integer getAmSelfControl() {
        return amSelfControl;
    }

    public void setAmSelfControl(Integer amSelfControl) {
        this.amSelfControl = amSelfControl;
    }

    public Integer getAmInitiative() {
        return amInitiative;
    }

    public void setAmInitiative(Integer amInitiative) {
        this.amInitiative = amInitiative;
    }

    public Integer getAmMovement() {
        return amMovement;
    }

    public void setAmMovement(Integer amMovement) {
        this.amMovement = amMovement;
    }

    public Integer getAmTravelMult() {
        return amTravelMult;
    }

    public void setAmTravelMult(Integer amTravelMult) {
        this.amTravelMult = amTravelMult;
    }

    public Integer getAmChoice() {
        return amChoice;
    }

    public void setAmChoice(Integer choice) {
        this.amChoice = choice;
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
                .add("abilityMods='" + getAbilityMods() + "'")
                .toString();
    }

    public boolean deepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerSet powerSet = (PowerSet) o;
        return Objects.equals(ssid, powerSet.ssid)
                && Objects.equals(name, powerSet.name)
                && Objects.equals(updated, powerSet.updated)
                && Objects.equals(openText, powerSet.openText)
                && Objects.equals(abilityText, powerSet.abilityText)
                && Objects.equals(powersText, powerSet.powersText)
                && Objects.equals(amPerception, powerSet.amPerception)
                && Objects.equals(amStealth, powerSet.amStealth)
                && Objects.equals(amAim, powerSet.amAim)
                && Objects.equals(amDodge, powerSet.amDodge)
                && Objects.equals(amInfluence, powerSet.amInfluence)
                && Objects.equals(amSelfControl, powerSet.amSelfControl)
                && Objects.equals(amInitiative, powerSet.amInitiative)
                && Objects.equals(amMovement, powerSet.amMovement)
                && Objects.equals(amTravelMult, powerSet.amTravelMult)
                && Objects.equals(amChoice, powerSet.amChoice);
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
    private String abilityMods;
    public String getAbilityMods() {
        if (abilityMods == null) {
            abilityMods = AbilityModsRenderer.renderAmString(this, amChoice);
        }
        return abilityMods;
    }

}
