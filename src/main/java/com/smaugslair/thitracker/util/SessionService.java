package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class SessionService {
    private Long gameId;
    private PlayerCharacter pc;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public PlayerCharacter getPc() {
        return pc;
    }

    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
    }
}