package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.ui.*;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.util.RepoService;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("GameMaster Session")
@Route(value = "gmsession", layout = MainView.class)
public class GMSession extends SplitLayout {

    private final TimeLineHistory timeLineHistory;

    public GMSession(RepoService repoService, SessionService sessionService) {

        GMTimeLineView gmTimeLineView = new GMTimeLineView(this, repoService, sessionService);

        SplitLayout historyLayout = new SplitLayout();
        historyLayout.setOrientation(Orientation.VERTICAL);
        historyLayout.addToPrimary(new DiceHistory(repoService, sessionService));
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
}
