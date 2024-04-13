package com.smaugslair.thitracker.data.powers;

import com.smaugslair.thitracker.rules.Ability;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class PowerSetMod implements Mod {

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
    @JoinColumn(name = "power_set_id")
    private PowerSet powerSet;

    public PowerSetMod() {
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

    public PowerSet getPowerSet() {
        return powerSet;
    }

    public void setPowerSet(PowerSet powerSet) {
        this.powerSet = powerSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PowerSetMod powerMod = (PowerSetMod) o;
        return ability == powerMod.ability && value.equals(powerMod.value) && powerSet.equals(powerMod.powerSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ability, value, powerSet);
    }
}
