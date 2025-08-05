package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.components.ValidTextField;
import com.smaugslair.thitracker.ui.components.events.HeroCountEvent;
import com.smaugslair.thitracker.ui.players.PCManager;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@PermitAll()
@Route(value = "hero", layout = MainView.class)
public class HeroView extends HorizontalLayout implements HasUrlParameter<String> {

    private PlayerCharacter pc;

    private static final Logger log = LoggerFactory.getLogger(HeroView.class);

    private final SessionService sessionService;

    public HeroView(SessionService sessionService) {
        this.sessionService = sessionService;
        addListener(HeroCountEvent.class, sessionService.getHeroCountListener()::heroCountChanged);
    }

    private void init() {
        removeAll();
        CharacterSheet characterSheet = new CharacterSheet(this::updatePC, true, sessionService);
        characterSheet.setPc(pc);
        add(new HorizontalLayout(new PCManager(sessionService, pc)));
        add(characterSheet);
        sessionService.getTitleBar().setTitle(pc.getName());
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

    @Override
    public void setParameter(BeforeEvent beforeEvent, String heroId) {
        log.info("setParameter, heroId:"+heroId);

        if (heroId != null && "new".equals(heroId)) {

            ValidTextField pcName = new ValidTextField();
            pcName.addValidator(new StringLengthValidator("2 char min", 2, 40));
            pcName.setPlaceholder("Character name");

            ConfirmDialog confirmDialog = new ConfirmDialog(pcName);

            Button saveButton = new UserSafeButton("Save", event -> {
                if (pcName.isValid()) {
                    pc = new PlayerCharacter();
                    pc.setName(pcName.getValue());
                    pc.setUserId(SecurityUtils.getLoggedInUser(sessionService).getId());
                    for (int i = 1; i < 4; ++i) {
                        for (TraitType type: TraitType.values()) {
                            Trait trait = new Trait();
                            trait.setSortOrder(type == TraitType.Hero ? i : i+3);
                            trait.setType(type);
                            trait.setName(type.name() + " trait " + i);
                            trait.setPoints(0);
                            trait.setPlayerCharacter(pc);
                            pc.getTraits().add(trait);
                        }
                    }
                    pc = sessionService.getPcRepo().save(pc);
                    confirmDialog.close();
                    getEventBus().fireEvent(new HeroCountEvent(this, false));
                    getUI().ifPresent(ui -> ui.navigate(HeroView.class, String.valueOf(pc.getId())));
                    //init();
                }
            });
            confirmDialog.setConfirmButton(saveButton);
            confirmDialog.open();
        }
        else {
            Optional<PlayerCharacter> optionalPC = sessionService.getPcRepo().findById(Long.valueOf(heroId));
            if (optionalPC.isPresent()) {
                pc = optionalPC.get();
                init();
            }
        }
    }

}
