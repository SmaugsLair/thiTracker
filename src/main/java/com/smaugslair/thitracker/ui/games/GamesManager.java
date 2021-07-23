package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.ValidTextField;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GamesManager extends VerticalLayout {


    private static final Logger log = LoggerFactory.getLogger(GamesManager.class);

    private final SessionService sessionService;

    public GamesManager(SessionService sessionService) {
        this.sessionService = sessionService;
        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        add(new Span("List of Games:"));

        Accordion accordion = new Accordion();

        User user = SecurityUtils.getLoggedInUser();
        if (user == null) return;

        Iterable<Game> games = sessionService.getGameRepo().findGamesByGameMasterId(user.getId());

        for (Game game : games) {
            accordion.add(game.getName(), getGameRow(game));
        }
        accordion.close();

        add(accordion);


        Button button = new Button("Create new Game");
        add(button);


        ValidTextField gameName = new ValidTextField();
        gameName.addValidator(new StringLengthValidator("2 char min", 2, 40));
        gameName.setPlaceholder("Game name");

        ConfirmDialog dialog = new ConfirmDialog(gameName);


        Button confirmButton = new Button("Save", event -> {
            if (gameName.isValid()) {
                Game game = new Game();
                game.setName(gameName.getValue());
                game.setGameMasterId(SecurityUtils.getLoggedInUser().getId());
                sessionService.getGameRepo().save(game);
                dialog.close();
                refresh();
            }
        });
        dialog.setConfirmButton(confirmButton);

        button.addClickListener(event -> dialog.open());
    }

    private HorizontalLayout getGameRow(Game game) {

        ConfirmDialog deleteDialog = new ConfirmDialog("Are you sure you want to delete the game "+game.getName()+"?");
        Button confirmButton = new Button("Delete", event -> {
            List<PlayerCharacter> pcs = sessionService.getPcRepo().findAllByGameId(game.getId());
            pcs.forEach(pc -> pc.setGameId(null));
            sessionService.getPcRepo().saveAll(pcs);
            sessionService.getTliRepo().deleteAllByGameId(game.getId());
            sessionService.getGameRepo().delete(game);
            deleteDialog.close();
            refresh();
        });
        deleteDialog.setConfirmButton(confirmButton);

        HorizontalLayout layout = new HorizontalLayout();
        Button launch = new Button("Launch");
        launch.addClickListener(e -> {
            launch.getUI().ifPresent(ui -> {
                sessionService.setGameId(game.getId());
                ui.navigate("gmsession");
                //ui.navigate(GMTimeLineView.class, new RouteParameters("gameId", game.getId().toString())));
            });
        });
        layout.add(launch);
        Button delete = new Button("Delete", event -> deleteDialog.open());
        layout.add(delete);
        return layout;
    }

}
