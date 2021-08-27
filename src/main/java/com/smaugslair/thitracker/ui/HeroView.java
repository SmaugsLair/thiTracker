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

@Route(value = "", layout = MainView.class)
public class HeroView extends HorizontalLayout {

    private final SessionService sessionService;

    public HeroView(SessionService sessionService) {
        this.sessionService = sessionService;
        CharacterSheet characterSheet = new CharacterSheet(this::updatePC, sessionService);

        add(new PCManager(sessionService, characterSheet::setPc));
        add(characterSheet);
    }

    private PlayerCharacter updatePC(PlayerCharacter pc) {

        pc = sessionService.getPcRepo().save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(pc.getGameId());
        Broadcaster.broadcast(entry);
        return pc;
    }
}
