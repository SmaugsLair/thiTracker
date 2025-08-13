package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;

import java.util.Optional;

public abstract class AbstractGameView extends VerticalLayout implements HasUrlParameter<Long> {


    protected final UIService uiService;
    protected final GameRepository gameRepository;
    //protected final PlayerCharacterRepository playerCharacterRepository;
    protected final TitleBar titleBar;
    protected Game game = null;

    protected AbstractGameView(UIService uiService, GameRepository gameRepository, TitleBar titleBar) {
        this.uiService = uiService;
        this.gameRepository = gameRepository;
        //this.playerCharacterRepository = playerCharacterRepository;
        this.titleBar = titleBar;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isPresent()) {
            game = optionalGame.get();
            uiService.setGame(game);
            init();
        }
    }

    protected abstract void init();

}
