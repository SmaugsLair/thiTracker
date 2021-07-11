package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Player Session")
@Route(value = "playersession", layout = MainView.class)
public class PlayerSession extends SplitLayout {

    public PlayerSession(GameRepository gameRepository,
                         TimeLineItemRepository tliRepository,
                         CollectedItemRepository collectedItemRepository,
                         PlayerCharacterRepository playerCharacterRepository,
                         SessionService sessionService,
                         EntryRepository entryRepository) {
        SplitLayout rollLayout = new SplitLayout();
        rollLayout.setOrientation(Orientation.VERTICAL);
        rollLayout.addToPrimary(new DiceRoller(sessionService, entryRepository));
        rollLayout.addToSecondary(new DiceHistory(sessionService, entryRepository));
        addToPrimary(rollLayout);
        addToSecondary(new PCTimeLineView(gameRepository, tliRepository, collectedItemRepository, playerCharacterRepository, sessionService));
        setHeightFull();
        setSplitterPosition(33);
    }
}
