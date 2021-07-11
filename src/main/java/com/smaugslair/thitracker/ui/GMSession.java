package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.ui.*;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("GameMaster Session")
@Route(value = "gmsession", layout = MainView.class)
public class GMSession extends SplitLayout {

    public GMSession(GameRepository gameRepository,
                     TimeLineItemRepository tliRepository,
                     CollectedItemRepository collectedItemRepository,
                     PlayerCharacterRepository playerCharacterRepository,
                     SessionService sessionService,
                     EntryRepository entryRepository) {

        SplitLayout historyLayout = new SplitLayout();
        historyLayout.setOrientation(Orientation.VERTICAL);
        historyLayout.addToPrimary(new DiceHistory(sessionService, entryRepository));
        historyLayout.addToSecondary(new TimeLineHistory(sessionService, entryRepository));
        addToPrimary(historyLayout);
        addToSecondary(new GMTimeLineView(gameRepository, tliRepository, collectedItemRepository, playerCharacterRepository, sessionService, entryRepository));
        setHeightFull();
        setSplitterPosition(25);
    }
}
