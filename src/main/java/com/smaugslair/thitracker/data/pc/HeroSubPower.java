package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.powers.Power;
import jakarta.persistence.*;

@Entity
public class HeroSubPower {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "p_ssid")
    private Power power;

    @ManyToOne
    @JoinColumn(name = "hp_id")
    private HeroPower heroPower;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public HeroPower getHeroPower() {
        return heroPower;
    }

    public void setHeroPower(HeroPower heroPower) {
        this.heroPower = heroPower;
    }

    public String getName() {
        return power.getName();
    }

    public String getShortDescr() {
        return power.getShortDescr();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HeroSubPower{");
        sb.append("id=").append(id);
        sb.append(", power=").append(power);
        sb.append(", heroPower=").append(heroPower);
        sb.append('}');
        return sb.toString();
    }

}
