package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.powers.Mod;
import com.smaugslair.thitracker.rules.Ability;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class HeroPowerSetMod implements Mod {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Ability ability;

    @Column
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "hero_power_set_id")
    private HeroPowerSet heroPowerSet;

    public HeroPowerSetMod() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public HeroPowerSet getHeroPowerSet() {
        return heroPowerSet;
    }

    public void setHeroPowerSet(HeroPowerSet heroPowerSet) {
        this.heroPowerSet = heroPowerSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeroPowerSetMod powerMod = (HeroPowerSetMod) o;
        return ability == powerMod.ability && value.equals(powerMod.value) && heroPowerSet.equals(powerMod.heroPowerSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ability, value, heroPowerSet);
    }
}
