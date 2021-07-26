package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.ThiEntity;
import com.smaugslair.thitracker.data.user.User;

import javax.persistence.*;

@Entity
public class PlayerCharacter implements ThiEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = true)
    private Long gameId;

    @Column(nullable = false)
    private Integer progressionTokens = 0;

    @Column(nullable = false)
    private Integer heroPoints = 0;

    @Column(nullable = false)
    private Integer dramaPoints = 0;


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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProgressionTokens() {
        return progressionTokens;
    }

    public void setProgressionTokens(Integer progressionTokens) {
        this.progressionTokens = progressionTokens;
    }

    public Integer getHeroPoints() {
        return heroPoints;
    }

    public void setHeroPoints(Integer heroPoints) {
        this.heroPoints = heroPoints;
    }

    public Integer getDramaPoints() {
        return dramaPoints;
    }

    public void setDramaPoints(Integer dramaPoints) {
        this.dramaPoints = dramaPoints;
    }

    @Override
    public PlayerCharacter createEmptyObject() {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setDramaPoints(null);
        pc.setHeroPoints(null);
        pc.setProgressionTokens(null);
        return pc;
    }

    @Transient
    String pcPlayerName = null;

    public String getCharacterAndPlayerName(User user) {
        if (pcPlayerName == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" (").append(user.getDisplayName()).append(")");
            pcPlayerName = sb.toString();
        }
        return pcPlayerName;
    }
}
