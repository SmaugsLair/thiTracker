package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.powers.Mod;
import com.smaugslair.thitracker.rules.Ability;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class HeroPowerMod implements Mod {

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
    @JoinColumn(name = "hero_power_id")
    private HeroPower heroPower;

    public HeroPowerMod() {
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

    public HeroPower getHeroPower() {
        return heroPower;
    }

    public void setHeroPower(HeroPower heroPower) {
        this.heroPower = heroPower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeroPowerMod powerMod = (HeroPowerMod) o;
        return ability == powerMod.ability && value.equals(powerMod.value) && heroPower.equals(powerMod.heroPower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ability, value, heroPower);
    }
}
