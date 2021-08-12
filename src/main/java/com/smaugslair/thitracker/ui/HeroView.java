package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.players.PCManager;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
public class HeroView extends HorizontalLayout {

    public HeroView(SessionService sessionService, CacheService cacheService) {

        CharacterSheet characterSheet = new CharacterSheet(this::updatePC, cacheService, sessionService);

        add(new PCManager(sessionService, cacheService, characterSheet::setPc));
        add(characterSheet);
    }

    private PlayerCharacter updatePC(PlayerCharacter playerCharacter) {
        return playerCharacter;
    }
}
