package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.smaugslair.thitracker.ui.components.events.AppEventListener;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

@Component
@PermitAll
@PageTitle("GameMaster Session")
@Route(value = "gmsession", layout = MainView.class)
@UIScope
public class GMSession extends AbstractGameView implements AppEventListener  {

    //private static final Logger log = LoggerFactory.getLogger(GMSession.class);

    private final TimeLineHistory timeLineHistory;
    private final GMTimeLineView gmTimeLineView;

    private GMCharacterView characterSheet;
    private DiceHistory diceHistory;

    private final SplitLayout splitLayout = new SplitLayout();


    protected GMSession(UIService uiService, GameRepository gameRepository, TitleBar titleBar, TimeLineHistory timeLineHistory,
                        GMTimeLineView gmTimeLineView) {
        super(uiService, gameRepository, titleBar);
        this.timeLineHistory = timeLineHistory;
        this.gmTimeLineView = gmTimeLineView;

        //log.info("GMSession created");
        uiService.addAppEventListener(this);

        add(splitLayout);
        splitLayout.setSizeFull();
        setHeightFull();
        setWidthFull();
    }

    public void init() {
        //log.info("GMSession init");
        splitLayout.removeAll();

        characterSheet = new GMCharacterView();
        //characterSheet.setEditablePowers(false);
        characterSheet.setPcUpdater(gmTimeLineView);
        //timeLineHistory = new TimeLineHistory();

        SplitLayout historyLayout = new SplitLayout(SplitLayout.Orientation.HORIZONTAL);
        historyLayout.setSplitterPosition(50);
        diceHistory = new DiceHistory(uiService.getGame().getId());
        historyLayout.addToPrimary(diceHistory);
        historyLayout.addToSecondary(timeLineHistory);

        SplitLayout timelineLayout = new SplitLayout(SplitLayout.Orientation.VERTICAL);
        timelineLayout.setSplitterPosition(75);
        timelineLayout.addToPrimary(gmTimeLineView);
        timelineLayout.addToSecondary(historyLayout);

        splitLayout.addToPrimary(timelineLayout);
        splitLayout.addToSecondary(characterSheet);
        splitLayout.setSplitterPosition(80);
        titleBar.removeAll();
        titleBar.add(new GMActionBar(this, uiService.getGame()));
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

    @Override
    public void onAppEvent(AppEvent e) {
        if (AppEvent.AppEventType.GameChosen.equals(e.getType())) {
            init();
        }
    }
}
