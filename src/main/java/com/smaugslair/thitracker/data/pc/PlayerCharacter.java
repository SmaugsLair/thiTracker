package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.user.User;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
public class PlayerCharacter {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = true)
    private Long gameId;


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

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCharacterAndPlayerName() {
        return getName() + " (" + user.getDisplayName() + ")";
    }
}
