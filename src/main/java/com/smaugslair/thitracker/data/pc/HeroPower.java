package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.util.AbilityModsRenderer;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Entity
public class HeroPower implements Moddable, Comparable<HeroPower> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PlayerCharacter playerCharacter;

    @ManyToOne
    @JoinColumn(name = "p_ssid")
    private Power power;

    @ManyToOne
    @JoinColumn(name = "hps_id")
    private HeroPowerSet heroPowerSet;

    @Column(nullable = false)
    private Integer tier;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_power_id" )
    @MapKey(name = "ability")
    Map<Ability, HeroPowerMod> mods = new HashMap<>();

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hp_id" )
    Set<HeroSubPower> subPowers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
    }

    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public HeroPowerSet getHeroPowerSet() {
        return heroPowerSet;
    }

    public void setHeroPowerSet(HeroPowerSet heroPowerSet) {
        this.heroPowerSet = heroPowerSet;
    }

    public Map<Ability, HeroPowerMod> getMods() {
        return mods;
    }

    public void setMods(Map<Ability, HeroPowerMod> mods) {
        this.mods = mods;
    }

    @Override
    public void addMod(Ability ability, int value) {
        HeroPowerMod mod = new HeroPowerMod();
        mod.setHeroPower(this);
        mod.setValue(value);
        mod.setAbility(ability);
        mods.put(ability, mod);
    }

    public String getPowerSetName() {
        return heroPowerSet.getPowerSet().getName();
    }

    public String getName() {
        return power.getName();
    }

    public String getShortDescr() {
        return power.getShortDescr();
    }

    public String getModText() {
        return AbilityModsRenderer.renderAmString(mods.values());
    }

    @Override
    public String toString() {
        String sb = "HeroPower{" + "id=" + id +
                ", playerCharacter=" + playerCharacter +
                ", power=" + power +
                ", heroPowerSet=" + heroPowerSet +
                ", mods=" + mods +
                '}';
        return sb;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }


    public Set<HeroSubPower> getSubPowers() {
        return subPowers;
    }

    public void setSubPowers(Set<HeroSubPower> subPowers) {
        this.subPowers = subPowers;
    }

    @Override
    public int compareTo(@NotNull HeroPower o) {
        return Comparator.comparing(HeroPower::getTier)
                .thenComparing(HeroPower::getPower)
                .thenComparing(HeroPower::getId)
                .compare(this, o);
    }

}
