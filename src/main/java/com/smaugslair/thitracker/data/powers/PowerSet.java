package com.smaugslair.thitracker.data.powers;

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

    private String abilityMods = "";

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

    public String getAbilityMods() {
        return abilityMods;
    }

    public void setAbilityMods(String abilityMods) {
        this.abilityMods = abilityMods;
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
                .add("abilityMods='" + abilityMods + "'")
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
                && Objects.equals(abilityMods, powerSet.abilityMods);
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
}
