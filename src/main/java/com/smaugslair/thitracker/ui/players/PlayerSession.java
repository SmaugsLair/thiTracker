package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Player Session")
@Route(value = "playersession", layout = MainView.class)
public class PlayerSession extends SplitLayout {

    public PlayerSession(SessionService sessionService, CacheService cacheService) {
        SplitLayout pcLayout = new SplitLayout();
        pcLayout.addToPrimary(new CharacterSheet(sessionService, SecurityUtils.getLoggedInUser()));
        pcLayout.addToSecondary(new DiceRoller(sessionService, cacheService));
        pcLayout.setSplitterPosition(50);
        addToPrimary(pcLayout);

        SplitLayout gameLayout = new SplitLayout();
        gameLayout.addToPrimary(new PCTimeLineView(sessionService, cacheService));
        gameLayout.addToSecondary(new DiceHistory(sessionService, cacheService));
        gameLayout.setOrientation(Orientation.VERTICAL);
        gameLayout.setSplitterPosition(50);
        addToSecondary(gameLayout);
        setHeightFull();
        setSplitterPosition(66);
    }
}
