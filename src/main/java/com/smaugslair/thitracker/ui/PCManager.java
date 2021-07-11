package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.RouteParameters;

public class PCManager extends VerticalLayout {

    private final PlayerCharacterRepository pcRepo;
    private final SessionService sessionService;

    public PCManager(PlayerCharacterRepository playerCharacterRepository, SessionService sessionService) {
        pcRepo = playerCharacterRepository;
        this.sessionService = sessionService;

        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {
        add(new Text("List of PCs"));

        User user = SecurityUtils.getLoggedInUser();
        if (user == null) return;

        Iterable<PlayerCharacter> pcs = pcRepo.findAllByUserId(user.getId());

        Accordion accordion = new Accordion();
        for (PlayerCharacter pc : pcs) {
            accordion.add(pc.getName(), getPcRow(pc));
        }

        add(accordion);


        Button button = new Button("Create new character");
        add(button);

        Dialog dialog = new Dialog();

        ValidTextField pcName = new ValidTextField();
        pcName.addValidator(new StringLengthValidator("2 char min", 2, 40));
        pcName.setPlaceholder("Character name");
        dialog.add(pcName);

        Button confirmButton = new Button("Save", event -> {
            if (pcName.isValid()) {
                PlayerCharacter pc = new PlayerCharacter();
                pc.setName(pcName.getValue());
                pc.setUserId(SecurityUtils.getLoggedInUser().getId());
                pcRepo.save(pc);
                dialog.close();
                refresh();
            }
        });
        dialog.add(confirmButton);
        Button cancelButton = new Button("Cancel", event -> {
            dialog.close();
        });
        dialog.add(cancelButton);

        button.addClickListener(event -> dialog.open());
    }

    private HorizontalLayout getPcRow(PlayerCharacter pc) {

        Dialog deleteDialog = new Dialog();

        deleteDialog.add(new Text("Are you sure you want to delete the character "+pc.getName()));
        Button confirmButton = new Button("Delete", event -> {
            pcRepo.delete(pc);
            deleteDialog.close();
            refresh();
        });
        deleteDialog.add(confirmButton);
        Button cancelButton = new Button("Cancel", event -> {
            deleteDialog.close();
        });
        deleteDialog.add(cancelButton);

        HorizontalLayout layout = new HorizontalLayout();
        if (pc.getGameId() != null) {
            Button launch = new Button("Launch");
            launch.addClickListener(e -> {
                launch.getUI().ifPresent(ui -> {
                    sessionService.setGameId(pc.getGameId());
                    sessionService.setPc(pc);
                    ui.navigate("playersession");
                    //ui.navigate(PCTimeLineView.class, new RouteParameters("gameId", pc.getGameId().toString()))                        }
                });
            });
            layout.add(launch);
        }
        else {
            Icon icon = VaadinIcon.FROWN_O.create();
            icon.setSize("16px");
            layout.add(icon);
        }
        Button delete = new Button("Delete", event -> deleteDialog.open());
        layout.add(delete);
        return layout;
    }
}
