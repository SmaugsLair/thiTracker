package com.smaugslair.thitracker.data.pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
public class AbilityScore implements Comparable<AbilityScore> {

    private static final Logger log = LoggerFactory.getLogger(AbilityScore.class);

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PlayerCharacter playerCharacter;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Integer sortOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        log.info("setId");
        this.id = id;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(PlayerCharacter playerCharacter) {
        log.info("setPlayerCharacter");
        this.playerCharacter = playerCharacter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        log.info("setName");
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        log.info("setPoints");
        this.points = points;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        log.info("setSortOrder");
        this.sortOrder = sortOrder;
    }

    @Override
    public int compareTo(AbilityScore o) {
        return sortOrder.compareTo(o.sortOrder);
    }

    @Override
    public String toString() {
        return "AbilityScore{" +
                "id=" + id +
                ", playerCharacter=" + playerCharacter +
                ", name='" + name + '\'' +
                ", points=" + points +
                ", sortOrder=" + sortOrder +
                '}';
    }
}
