package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.rules.Ability;

import javax.persistence.*;

@Entity
public class AbilityScore {

    //private static final Logger log = LoggerFactory.getLogger(AbilityScore.class);

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PlayerCharacter playerCharacter;

    @Column(nullable = false)
    private Ability ability;

    @Column(nullable = false)
    private Integer points;

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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "AbilityScore{" +
                "id=" + id +
                ", playerCharacter=" + playerCharacter +
                ", ability='" + ability + '\'' +
                ", points=" + points +
                '}';
    }
}
