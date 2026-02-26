package com.smaugslair.thitracker.services;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.smaugslair.thitracker.ui.components.events.AppEventListener;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@UIScope
public class UIService {

    //private static final Logger log = LoggerFactory.getLogger(UIService.class);

    private Game game;
    private PlayerCharacter pc;

    private final PlayerCharacterRepository pcRepo;

    private final Map<Class<? extends AppEventListener>, AppEventListener> appEventListeners = new HashMap<>();

    public UIService(PlayerCharacterRepository pcRepo) {
        this.pcRepo = pcRepo;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        //log.info("setGame: "+game);
        this.game = game;
        fireEvent(new AppEvent(AppEvent.AppEventType.GameChosen, game.getId()));
    }

    public PlayerCharacter getPc() {
        return pc;
    }

    public void setPc(PlayerCharacter pc) {
        //log.info("setPc: "+pc);
        this.pc = pc;
        fireEvent(new AppEvent(AppEvent.AppEventType.HeroChosen, pc.getId()));
    }


    public void refreshPc() {
        if (pc != null) {
            pc = pcRepo.findById(pc.getId()).orElse(pc);
        }
    }


    public void addAppEventListener(AppEventListener listener) {
        //log.info("Adding app event listener:"+listener);
        appEventListeners.put(listener.getClass(), listener);
    }

    public void fireEvent(AppEvent event) {
        appEventListeners.forEach((aClass, appEventListener) -> {
            //log.info("Calling event listener:"+appEventListener);
            appEventListener.onAppEvent(event);
        });

    }
}
