package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.components.ValidTextField;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PCManager extends VerticalLayout {

    private final Logger log = LoggerFactory.getLogger(PCManager.class);

    private final SessionService sessionService;
    private final Consumer<PlayerCharacter> pcSelector;

    public PCManager(SessionService sessionService, Consumer<PlayerCharacter> pcSelector) {
        this.sessionService = sessionService;
        this.pcSelector = pcSelector;

        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        User user = SecurityUtils.getLoggedInUser(sessionService);
        if (user == null) return;

        Iterable<PlayerCharacter> pcs = sessionService.getPcRepo().findAllByUserId(user.getId())
                .stream().sorted().collect(Collectors.toList());

        Accordion accordion = new Accordion();
        for (PlayerCharacter pc : pcs) {
            Game game = null;
            if (pc.getGameId() != null) {
                game = sessionService.getGameRepo().findById(pc.getGameId()).orElse(null);
            }
            accordion.add(pc.getName()+
                    (game==null ? "" : " ("+game.getName()+")"), getPcRow(pc, game));
        }
        accordion.close();

        add(accordion);


        Button button = new UserSafeButton("Create new character");
        add(button);

        //Dialog dialog = new Dialog();

        ValidTextField pcName = new ValidTextField();
        pcName.addValidator(new StringLengthValidator("2 char min", 2, 40));
        pcName.setPlaceholder("Character name");

        ConfirmDialog confirmDialog = new ConfirmDialog(pcName);

        Button confirmButton = new UserSafeButton("Save", event -> {
            if (pcName.isValid()) {
                PlayerCharacter pc = new PlayerCharacter();
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
                sessionService.getPcRepo().save(pc);
                confirmDialog.close();
                refresh();
            }
        });
        confirmDialog.setConfirmButton(confirmButton);

        button.addClickListener(event -> confirmDialog.open());
    }

    private VerticalLayout getPcRow(PlayerCharacter pc, Game game) {

        ConfirmDialog deleteDialog = new ConfirmDialog("Are you sure you want to delete the character "+pc.getName()+"?");
        Button confirmButton = new UserSafeButton("Delete", event -> {
            List<TimeLineItem> items = sessionService.getTliRepo().findByPcId(pc.getId());
            items.forEach(item -> {
                item.setPcId(null);
                item.setName(pc.getName());
            });
            sessionService.getTliRepo().saveAll(items);
            sessionService.getHpsRepo().deleteAll(sessionService.getHpsRepo().findAllByPlayerCharacter(pc));
            sessionService.getPcRepo().delete(pc);
            deleteDialog.close();
            refresh();
        });
        deleteDialog.setConfirmButton(confirmButton);

        VerticalLayout layout = new VerticalLayout();
        if (pc.getGameId() != null) {
            Button launch = new UserSafeButton("Launch "+ game.getName());
            launch.addClickListener(e -> launch.getUI().ifPresent(ui -> {
                sessionService.setGameId(pc.getGameId());
                sessionService.setPc(pc);
                ui.navigate("playersession");
            }));
            layout.add(launch);
        }
        else {
            Icon icon = VaadinIcon.FROWN_O.create();
            icon.setSize("16px");
            icon.setTooltipText("Character has no game.");
            layout.add(icon);
        }

        Button sheet = new UserSafeButton("Edit sheet -->", event -> pcSelector.accept(pc));
        layout.add(sheet);


        Button printableSheet = new UserSafeButton("Printable Sheet", event -> {
            sessionService.setPc(pc);
            getUI().ifPresent(ui -> {
                ui.getPage().open("printableSheet", "Printable Character Sheet");
            });
        });
        layout.add(printableSheet);

        Button printablePowers = new UserSafeButton("Printable Powers", event -> {
            sessionService.setPc(pc);
            getUI().ifPresent(ui -> {
                ui.getPage().open("printablePowers", "Printable Character Powers");
            });
        });
        layout.add(printablePowers);


        Button delete = new UserSafeButton("Delete", event -> deleteDialog.open());
        layout.add(delete);

        return layout;
    }


}
