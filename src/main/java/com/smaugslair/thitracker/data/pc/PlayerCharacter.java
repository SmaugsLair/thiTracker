package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.ThiEntity;
import com.smaugslair.thitracker.data.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "trait_id" )
    @OrderBy("sortOrder ASC")
    List<Trait> traits = new ArrayList<>();


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

    public List<Trait> getTraits() {
        return traits;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }

    @Override
    public PlayerCharacter createEmptyObject() {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setTraits(null);
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

    public boolean isHeroPointsAvailable() {
        for (Trait trait : traits) {
            if (TraitType.Hero.equals(trait.getType())) {
                if (trait.getPoints() > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
