package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PermitAll
@PageTitle("Player Session")
@Route(value = "playersession", layout = MainView.class)
public class PlayerSession extends TabSheet {

    private static final Logger log = LoggerFactory.getLogger(PlayerSession.class);

    private final SessionService sessionService;

    public PlayerSession(SessionService sessionService) {
        this.sessionService = sessionService;
        PlayerCharacter pc = sessionService.getPc();
        if (pc == null) {
            setPrefixComponent(new Span("PC not chosen"));
            return;
        }
        Game game = sessionService.getGameRepo().findById(pc.getGameId()).orElse(null);
        if (game == null) {
            setPrefixComponent(new Span("This pc is not in a game"));
            return;
        }

        sessionService.getTitleBar().setTitle(sessionService.getPc().getName()+ " in " + game.getName());


        CharacterSheet characterSheet = new CharacterSheet(this::updatePc, false, sessionService);
        characterSheet.setPc(sessionService.getPc());
        characterSheet.setWidthFull();


        SplitLayout gameLayout = new SplitLayout();
        gameLayout.addToPrimary(new PCTimeLineView(sessionService));
        gameLayout.addToSecondary(new DiceHistory(sessionService));
        gameLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        gameLayout.setSplitterPosition(50);

        add("Timeline", gameLayout);
        add("Dice Roller", new DiceRollView(sessionService));
        add("Character Sheet", characterSheet);
        setHeightFull();
        setWidthFull();
    }

    public PlayerCharacter updatePc(PlayerCharacter pc) {
        log.info("updatePc");
        pc = sessionService.getPcRepo().save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(pc.getGameId());
        Broadcaster.broadcast(entry);
        return pc;
    }
}
