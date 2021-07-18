package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.ValidTextField;
import com.smaugslair.thitracker.util.RepoService;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.util.List;

public class PCManager extends VerticalLayout {

    private final RepoService repoService;
    private final SessionService sessionService;

    public PCManager(RepoService repoService, SessionService sessionService) {
        this.sessionService = sessionService;
        this.repoService = repoService;
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

        Iterable<PlayerCharacter> pcs = repoService.getPcRepo().findAllByUserId(user.getId());

        Accordion accordion = new Accordion();
        for (PlayerCharacter pc : pcs) {
            accordion.add(pc.getName(), getPcRow(pc));
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
                pc.setUser(SecurityUtils.getLoggedInUser());
                repoService.getPcRepo().save(pc);
                confirmDialog.close();
                refresh();
            }
        });
        confirmDialog.setConfirmButton(confirmButton);

        button.addClickListener(event -> confirmDialog.open());
    }

    private HorizontalLayout getPcRow(PlayerCharacter pc) {

        ConfirmDialog deleteDialog = new ConfirmDialog("Are you sure you want to delete the character "+pc.getName()+"?");
        Button confirmButton = new Button("Delete", event -> {
            List<TimeLineItem> items = repoService.getTliRepo().findAllByPcId(pc.getId());
            items.forEach(item -> {
                item.setPcId(null);
                item.setName(pc.getName());
            });
            repoService.getTliRepo().saveAll(items);
            repoService.getPcRepo().delete(pc);
            deleteDialog.close();
            refresh();
        });
        deleteDialog.setConfirmButton(confirmButton);

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
