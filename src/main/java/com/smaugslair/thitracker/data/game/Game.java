package com.smaugslair.thitracker.data.game;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer gameMasterId;

    @Column
    private Long lastEventId;


    @Column(nullable = false)
    private Integer maxDice = 10;

    @Column(nullable = false)
    private Integer powerSetLimit = 2;

    @Column(nullable = false)
    private Integer powerLimit = 9;

    public Game() {}

    public Game(String name) {
        setName(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGameMasterId() {
        return gameMasterId;
    }

    public void setGameMasterId(Integer gameMasterId) {
        this.gameMasterId = gameMasterId;
    }

    public Long getLastEventId() {
        return lastEventId;
    }

    public void setLastEventId(Long lastEventId) {
        this.lastEventId = lastEventId;
    }

    public Integer getMaxDice() {
        return maxDice;
    }

    public void setMaxDice(Integer maxDice) {
        this.maxDice = maxDice;
    }

    public Integer getPowerSetLimit() {
        return powerSetLimit;
    }

    public void setPowerSetLimit(Integer powerSetLimit) {
        this.powerSetLimit = powerSetLimit;
    }

    public Integer getPowerLimit() {
        return powerLimit;
    }

    public void setPowerLimit(Integer powerLimit) {
        this.powerLimit = powerLimit;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gameMasterId=" + gameMasterId +
                '}';
    }
/*
    @Override
    public Game createEmptyObject() {
        Game g = new Game();
        g.setMaxDice(null);
        return g;
    }*/
}
