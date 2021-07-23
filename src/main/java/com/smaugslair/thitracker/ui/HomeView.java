package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.ui.games.GamesManager;
import com.smaugslair.thitracker.ui.players.PCManager;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
public class HomeView extends HorizontalLayout {

    public HomeView(SessionService sessionService) {

        add(new PCManager(sessionService));
        add(new GamesManager(sessionService));
    }
}
