package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.players.AbstractHeroView;
import com.smaugslair.thitracker.ui.players.PCUpdater;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@PermitAll()
@Route(value = "hero", layout = MainView.class)
@UIScope
public class HeroView extends AbstractHeroView implements PCUpdater {

    private static final Logger log = LoggerFactory.getLogger(HeroView.class);


    protected HeroView(UIService uiService, GameRepository gameRepository, PlayerCharacterRepository playerCharacterRepository, TitleBar titleBar) {
        super(uiService, gameRepository, playerCharacterRepository, titleBar);
    }

    protected void init() {
        removeAll();
        CharacterSheet characterSheet = new CharacterSheet();
        characterSheet.setPcUpdater(this);
        //characterSheet.setEditablePowers(true);
        characterSheet.setPc(hero);
        //add(new HorizontalLayout(new PCManager(sessionService, pc)));
        add(characterSheet);
        titleBar.setTitle(hero.getName());
    }

    @Override
    public PlayerCharacter updatePc(PlayerCharacter pc) {
        log.info("updatePC");

        pc = playerCharacterRepository.save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(pc.getGameId());
        Broadcaster.broadcast(entry);
        return pc;
    }
}
