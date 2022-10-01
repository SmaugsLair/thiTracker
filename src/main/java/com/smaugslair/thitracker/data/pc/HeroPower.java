package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.util.AbilityModsRenderer;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class HeroPower implements Moddable {

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

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_power_id" )
    @MapKey(name = "ability")
    Map<Ability, HeroPowerMod> mods = new HashMap<>();


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
        final StringBuilder sb = new StringBuilder("HeroPower{");
        sb.append("id=").append(id);
        sb.append(", playerCharacter=").append(playerCharacter);
        sb.append(", power=").append(power);
        sb.append(", heroPowerSet=").append(heroPowerSet);
        sb.append(", mods=").append(mods);
        sb.append('}');
        return sb.toString();
    }
}
