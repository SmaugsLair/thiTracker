package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.util.SessionService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamesManager extends VerticalLayout {


    private static final Logger log = LoggerFactory.getLogger(GamesManager.class);
    private final TimeLineItemRepository tliRepo;
    private final GameRepository gameRepo;
    private final SessionService sessionService;

    public GamesManager(GameRepository gameRepository, TimeLineItemRepository tliRepository, SessionService sessionService) {
        gameRepo = gameRepository;
        tliRepo = tliRepository;
        this.sessionService = sessionService;
        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        add(new Text("List of Games:"));

        Accordion accordion = new Accordion();

        User user = SecurityUtils.getLoggedInUser();
        if (user == null) return;

        Iterable<Game> games = gameRepo.findGamesByGameMasterId(user.getId());

        for (Game game : games) {
            accordion.add(game.getName(), getGameRow(game));
        }

        add(accordion);


        Button button = new Button("Create new Game");
        add(button);


        Dialog dialog = new Dialog();

        ValidTextField gameName = new ValidTextField();
        gameName.addValidator(new StringLengthValidator("2 char min", 2, 40));
        gameName.setPlaceholder("Game name");
        dialog.add(gameName);

        Button confirmButton = new Button("Save", event -> {
            if (gameName.isValid()) {
                Game game = new Game();
                game.setName(gameName.getValue());
                game.setGameMasterId(SecurityUtils.getLoggedInUser().getId());
                gameRepo.save(game);
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

    private HorizontalLayout getGameRow(Game game) {

        Dialog deleteDialog = new Dialog();

        deleteDialog.add(new Text("Are you sure you want to delete the game "+game.getName()));
        Button confirmButton = new Button("Delete", event -> {
            tliRepo.deleteAllByGameId(game.getId());
            gameRepo.delete(game);
            deleteDialog.close();
            refresh();
        });
        deleteDialog.add(confirmButton);
        Button cancelButton = new Button("Cancel", event -> {
            deleteDialog.close();
        });
        deleteDialog.add(cancelButton);

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
