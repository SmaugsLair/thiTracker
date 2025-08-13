package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;

import java.util.Optional;

public abstract class AbstractHeroView extends VerticalLayout implements HasUrlParameter<Long> {


    protected final UIService uiService;
    protected final GameRepository gameRepository;
    protected final PlayerCharacterRepository playerCharacterRepository;
    protected final TitleBar titleBar;
    protected PlayerCharacter hero;

    protected AbstractHeroView(UIService uiService, GameRepository gameRepository, PlayerCharacterRepository playerCharacterRepository, TitleBar titleBar) {
        this.uiService = uiService;
        this.gameRepository = gameRepository;
        this.playerCharacterRepository = playerCharacterRepository;
        this.titleBar = titleBar;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long heroId) {
        Optional<PlayerCharacter> optionalPC = playerCharacterRepository.findById(heroId);
        if (optionalPC.isPresent()) {
            hero = optionalPC.get();
            uiService.setPc(hero);
            init();
        }
    }

    protected abstract void init();

}
