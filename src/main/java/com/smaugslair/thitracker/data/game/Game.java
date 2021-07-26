package com.smaugslair.thitracker.data.game;

import com.smaugslair.thitracker.data.ThiEntity;

import javax.persistence.*;

@Entity
public class Game implements ThiEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer gameMasterId;

    @Column
    private Long lastEventId;

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

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gameMasterId=" + gameMasterId +
                '}';
    }

    @Override
    public Game createEmptyObject() {
        return new Game();
    }
}
