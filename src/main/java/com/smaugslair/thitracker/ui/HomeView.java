package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.ui.games.GamesManager;
import com.smaugslair.thitracker.ui.players.PCManager;
import com.smaugslair.thitracker.util.RepoService;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
public class HomeView extends HorizontalLayout {

    public HomeView(RepoService repoService, SessionService sessionService) {

        add(new PCManager(repoService, sessionService));
        add(new GamesManager(repoService, sessionService));
    }
}
