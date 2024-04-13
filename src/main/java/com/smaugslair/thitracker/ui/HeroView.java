package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.players.PCManager;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PermitAll()
@Route(value = "", layout = MainView.class)
public class HeroView extends HorizontalLayout {

    private static final Logger log = LoggerFactory.getLogger(HeroView.class);

    private final SessionService sessionService;

    public HeroView(SessionService sessionService) {
        this.sessionService = sessionService;
        CharacterSheet characterSheet = new CharacterSheet(this::updatePC, true, sessionService);

        add(new HorizontalLayout(new PCManager(sessionService, characterSheet::setPc)));
        add(characterSheet);
        sessionService.getTitleBar().setTitle("Heroes");
    }

    private PlayerCharacter updatePC(PlayerCharacter pc) {
        log.info("updatePC");

        pc = sessionService.getPcRepo().save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(pc.getGameId());
        Broadcaster.broadcast(entry);
        return pc;
    }
}
