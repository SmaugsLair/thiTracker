package com.smaugslair.thitracker.data.pc;

import javax.persistence.*;

@Entity
public class AbilityScore implements Comparable<AbilityScore> {

    //private static final Logger log = LoggerFactory.getLogger(AbilityScore.class);

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
        this.id = id;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
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
