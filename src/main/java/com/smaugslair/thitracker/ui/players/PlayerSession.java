package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Player Session")
@Route(value = "playersession", layout = MainView.class)
public class PlayerSession extends SplitLayout {

    public PlayerSession(SessionService sessionService) {
        SplitLayout pcLayout = new SplitLayout();
        pcLayout.addToPrimary(new CharacterSheet(sessionService));
        pcLayout.addToSecondary(new DiceRoller(sessionService));
        pcLayout.setSplitterPosition(50);
        addToPrimary(pcLayout);

        SplitLayout gameLayout = new SplitLayout();
        gameLayout.addToPrimary(new PCTimeLineView(sessionService));
        gameLayout.addToSecondary(new DiceHistory(sessionService));
        gameLayout.setOrientation(Orientation.VERTICAL);
        gameLayout.setSplitterPosition(50);
        addToSecondary(gameLayout);
        setHeightFull();
        setSplitterPosition(66);
    }
}
