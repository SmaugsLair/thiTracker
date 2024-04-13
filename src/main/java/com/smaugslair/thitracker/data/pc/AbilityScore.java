package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.rules.Ability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.*;

@Entity
public class AbilityScore {

    private static final Logger log = LoggerFactory.getLogger(AbilityScore.class);

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PlayerCharacter playerCharacter;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ability ability;

    @Column(nullable = false)
    private Integer base;

    @Column(nullable = false)
    private Integer mods;

    public AbilityScore() {
    }

    public AbilityScore(Ability ability, PlayerCharacter pc) {
        this.ability = ability;
        this.base = ability.getBaseValue();
        mods = 0;
        playerCharacter = pc;
    }

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

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public Integer getMods() {
        return mods;
    }

   public void reset() {
        mods = 0;
   }

    public void adjustMods(int delta) {
        //log.info("adjustMods: " + this + " by " + delta);
        mods = mods+delta;
    }

    public int getPoints() {
        return getBase() + getMods();

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbilityScore{");
        sb.append("id=").append(id);
        sb.append(", playerCharacter=").append(playerCharacter.getName());
        sb.append(", ability=").append(ability.name());
        sb.append(", base=").append(base);
        sb.append(", mods=").append(mods);
        sb.append('}');
        return sb.toString();
    }
}
