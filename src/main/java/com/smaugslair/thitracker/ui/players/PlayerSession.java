package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.util.RepoService;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Player Session")
@Route(value = "playersession", layout = MainView.class)
public class PlayerSession extends SplitLayout {

    public PlayerSession(RepoService repoService, SessionService sessionService) {
        SplitLayout rollLayout = new SplitLayout();
        rollLayout.setOrientation(Orientation.VERTICAL);
        rollLayout.addToPrimary(new DiceRoller(repoService, sessionService));
        rollLayout.addToSecondary(new DiceHistory(repoService, sessionService));
        addToPrimary(rollLayout);
        addToSecondary(new PCTimeLineView(repoService, sessionService));
        setHeightFull();
        setSplitterPosition(33);
    }
}
