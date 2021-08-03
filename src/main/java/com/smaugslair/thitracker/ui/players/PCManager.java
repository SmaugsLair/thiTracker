package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.ValidTextField;
import com.smaugslair.thitracker.util.NameValue;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PCManager extends VerticalLayout {

    private final Logger log = LoggerFactory.getLogger(PCManager.class);

    private final SessionService sessionService;
    private final CacheService cacheService;
    private final TraitForm traitForm;
    private final ConfirmDialog editTraitsDialog;

    public PCManager(SessionService sessionService, CacheService cacheService) {
        this.sessionService = sessionService;
        this.cacheService = cacheService;

        traitForm = new TraitForm();
        editTraitsDialog = new ConfirmDialog(traitForm);
        editTraitsDialog.setConfirmButton(traitForm.getSaveButton());
        traitForm.getSaveButton().addClickListener(event -> {
            if (traitForm.isValid()) {
                PlayerCharacter playerCharacter = traitForm.getPC();
                sessionService.getCacheService().getPcCache().save(playerCharacter);
                editTraitsDialog.close();
                Entry entry = new Entry(EventType.PlayerPCUpdate);
                entry.setPcId(playerCharacter.getId());
                Broadcaster.broadcast(entry);
            }

        });
        editTraitsDialog.setWidth("500px");

        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {
        add(new Span("List of PCs"));

        User user = SecurityUtils.getLoggedInUser();
        if (user == null) return;

        NameValue nameValue = new NameValue("userId", user.getId());

        Iterable<PlayerCharacter> pcs = cacheService.getPcCache().findManyByProperty(nameValue);

        Accordion accordion = new Accordion();
        for (PlayerCharacter pc : pcs) {
            Game game = cacheService.getGameCache().findOneById(pc.getGameId()).orElse(null);
            accordion.add(pc.getName()+
                    (game==null ? "" : " ("+game.getName()+")"), getPcRow(pc, game));
        }
        accordion.close();

        add(accordion);


        Button button = new Button("Create new character");
        add(button);

        //Dialog dialog = new Dialog();

        ValidTextField pcName = new ValidTextField();
        pcName.addValidator(new StringLengthValidator("2 char min", 2, 40));
        pcName.setPlaceholder("Character name");

        ConfirmDialog confirmDialog = new ConfirmDialog(pcName);

        Button confirmButton = new Button("Save", event -> {
            if (pcName.isValid()) {
                PlayerCharacter pc = new PlayerCharacter();
                pc.setName(pcName.getValue());
                pc.setUserId(SecurityUtils.getLoggedInUser().getId());
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
                pc.setTraits(pc.getTraits().stream().sorted().collect(Collectors.toList()));
                cacheService.getPcCache().save(pc);
                confirmDialog.close();
                refresh();
            }
        });
        confirmDialog.setConfirmButton(confirmButton);

        button.addClickListener(event -> confirmDialog.open());
    }

    private HorizontalLayout getPcRow(PlayerCharacter pc, Game game) {

        ConfirmDialog deleteDialog = new ConfirmDialog("Are you sure you want to delete the character "+pc.getName()+"?");
        Button confirmButton = new Button("Delete", event -> {
            NameValue example = new NameValue("pcId", pc.getId());
            List<TimeLineItem> items = cacheService.getTliCache().findManyByProperty(example);
            items.forEach(item -> {
                item.setPcId(null);
                item.setName(pc.getName());
            });
            cacheService.getTliCache().saveAll(items);
            cacheService.getPcCache().delete(pc);
            deleteDialog.close();
            refresh();
        });
        deleteDialog.setConfirmButton(confirmButton);

        //ConfirmDialog editDialog = new ConfirmDialog()

        HorizontalLayout layout = new HorizontalLayout();
        if (pc.getGameId() != null) {
            Button launch = new Button("Launch "+ game.getName());
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
            layout.add(icon);
        }
        Button edit = new Button("Edit traits", event -> {
            traitForm.setPc(pc);
            editTraitsDialog.open();
        });
        layout.add(edit);
        Button delete = new Button("Delete", event -> deleteDialog.open());
        layout.add(delete);
        return layout;
    }


}
