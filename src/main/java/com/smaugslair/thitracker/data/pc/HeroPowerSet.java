package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.rules.Ability;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class HeroPowerSet implements Moddable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PlayerCharacter playerCharacter;

    @ManyToOne
    @JoinColumn(name = "ps_ssid")
    private PowerSet powerSet;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_power_set_id" )
    @MapKey(name = "ability")
    Map<Ability, HeroPowerSetMod> mods = new HashMap<>();


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

    public PowerSet getPowerSet() {
        return powerSet;
    }

    public void setPowerSet(PowerSet powerSet) {
        this.powerSet = powerSet;
    }

    public Map<Ability, HeroPowerSetMod> getMods() {
        return mods;
    }

    public void setMods(Map<Ability, HeroPowerSetMod> mods) {
        this.mods = mods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeroPowerSet that = (HeroPowerSet) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void addMod(Ability ability, int value) {
        HeroPowerSetMod mod = new HeroPowerSetMod();
        mod.setHeroPowerSet(this);
        mod.setValue(value);
        mod.setAbility(ability);
        mods.put(ability, mod);
    }
}
