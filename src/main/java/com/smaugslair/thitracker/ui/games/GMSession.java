package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("GameMaster Session")
@Route(value = "gmsession", layout = MainView.class)
public class GMSession extends SplitLayout {

    private final TimeLineHistory timeLineHistory;
    private final DiceHistory diceHistory;
    private final CharacterSheet characterSheet;

    public GMSession(SessionService sessionService) {

        GMTimeLineView gmTimeLineView = new GMTimeLineView(this, sessionService);
        characterSheet = new CharacterSheet(gmTimeLineView::updatePc, false, sessionService);
        diceHistory = new DiceHistory(sessionService);
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

        addToPrimary(timelineLayout);
        addToSecondary(characterSheet);
        setHeightFull();
        setSplitterPosition(80);
        sessionService.getTitleBar().removeAll();
        sessionService.getTitleBar().add(new GMActionBar(sessionService, this));
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

    public void setHero(PlayerCharacter pc) {
        characterSheet.setPc(pc);
    }
}
