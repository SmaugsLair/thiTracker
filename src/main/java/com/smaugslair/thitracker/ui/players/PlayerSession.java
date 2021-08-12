package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Player Session")
@Route(value = "playersession", layout = MainView.class)
public class PlayerSession extends SplitLayout {

    private final CacheService cacheService;

    public PlayerSession(SessionService sessionService, CacheService cacheService) {
        this.cacheService = cacheService;
        SplitLayout pcLayout = new SplitLayout();
        CharacterSheet characterSheet = new CharacterSheet(this::updatePc, cacheService, sessionService);
        characterSheet.setPc(sessionService.getPc());
        pcLayout.addToPrimary(characterSheet);
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

    public PlayerCharacter updatePc(PlayerCharacter pc) {
        //log.info("updatePc");
        pc = cacheService.getPcCache().save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(pc.getGameId());
        Broadcaster.broadcast(entry);
        return pc;
    }
}
