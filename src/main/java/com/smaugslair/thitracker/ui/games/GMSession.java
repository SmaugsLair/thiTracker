package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("GameMaster Session")
@Route(value = "gmsession", layout = MainView.class)
public class GMSession extends SplitLayout {

    private final TimeLineHistory timeLineHistory;
    private final DiceHistory diceHistory;

    public GMSession(SessionService sessionService) {

        GMTimeLineView gmTimeLineView = new GMTimeLineView(this, sessionService);

        SplitLayout historyLayout = new SplitLayout();
        historyLayout.setOrientation(Orientation.VERTICAL);
        diceHistory = new DiceHistory(sessionService);
        historyLayout.addToPrimary(diceHistory);
        timeLineHistory = new TimeLineHistory(gmTimeLineView);
        historyLayout.addToSecondary(timeLineHistory);
        addToPrimary(historyLayout);

        addToSecondary(gmTimeLineView);
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
}
