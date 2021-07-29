package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("GameMaster Session")
@Route(value = "gmsession", layout = MainView.class)
public class GMSession extends SplitLayout {

    private final TimeLineHistory timeLineHistory;
    private final DiceHistory diceHistory;
    private final GMTimeLineView gmTimeLineView;
    private final CharacterDetails characterDetails;

    public GMSession(SessionService sessionService, CacheService cacheService) {

        gmTimeLineView = new GMTimeLineView(this, sessionService, cacheService);
        characterDetails = new CharacterDetails(sessionService, gmTimeLineView);
        diceHistory = new DiceHistory(sessionService, cacheService);
        timeLineHistory = new TimeLineHistory(gmTimeLineView);

        SplitLayout historyLayout = new SplitLayout();
        historyLayout.setOrientation(Orientation.HORIZONTAL);
        historyLayout.setSplitterPosition(50);
        historyLayout.addToPrimary(diceHistory);
        historyLayout.addToSecondary(timeLineHistory);

        SplitLayout timelineLayout = new SplitLayout();
        timelineLayout.setOrientation(Orientation.VERTICAL);
        timelineLayout.setSplitterPosition(75);
        timelineLayout.addToPrimary(gmTimeLineView);
        timelineLayout.addToSecondary(historyLayout);
        addToPrimary(characterDetails);

        addToSecondary(timelineLayout);
        setHeightFull();
        setSplitterPosition(25);
    }

    public void logAction(Entry entry) {
        timeLineHistory.addHistory(entry);
    }

    public void clearRolls() {
        diceHistory.removeAll();
    }

    public void clearActions() {
        timeLineHistory.removeAll();
    }

    public void setHero(PlayerCharacter pc, User user) {
        characterDetails.setPc(pc, user);
    }
}
