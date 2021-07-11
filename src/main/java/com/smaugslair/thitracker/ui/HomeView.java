package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
public class HomeView extends HorizontalLayout {

    public HomeView(GameRepository gameRepository,
                    TimeLineItemRepository tliRepo,
                    PlayerCharacterRepository pcRepo,
                    SessionService sessionService) {
        add(new PCManager(pcRepo, sessionService));
        add(new GamesManager(gameRepository, tliRepo, sessionService));
    }
}
