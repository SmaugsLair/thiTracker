package com.smaugslair.thitracker.data.pc;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class Trait implements Comparable<Trait>{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PlayerCharacter playerCharacter;

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(nullable = false)
    private TraitType type;

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

    public TraitType getType() {
        return type;
    }

    public void setType(TraitType type) {
        this.type = type;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer order) {
        this.sortOrder = order;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Trait.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("playerCharacter=" + playerCharacter)
                .add("sortOrder=" + sortOrder)
                .add("name='" + name + "'")
                .add("points=" + points)
                .add("type=" + type)
                .toString();
    }

    @Override
    public int compareTo(Trait o) {
        return sortOrder.compareTo(o.sortOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trait trait = (Trait) o;
        return Objects.equals(playerCharacter, trait.playerCharacter) && Objects.equals(sortOrder, trait.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerCharacter, sortOrder);
    }

    public boolean isDeletable() {
        return sortOrder > 6;
    }
}
