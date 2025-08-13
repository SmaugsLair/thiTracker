package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.*;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Game Creation")
@Route(value = "gamecreation", layout = MainView.class)
@UIScope
public class GameCreationView extends VerticalLayout {


    public GameCreationView(TitleBar titleBar, SessionService sessionService, UIService uiService, GameRepository gameRepository) {

        ValidTextField gameName = new ValidTextField();
        gameName.setLabel("Game name");
        IntegerField maxDice = new IntegerField();
        maxDice.setStepButtonsVisible(true);
        maxDice.setValue(10);
        maxDice.setMin(1);
        maxDice.setLabel("Max dice");
        gameName.addValidator(new StringLengthValidator("2 char min", 2, 40));
        gameName.setPlaceholder("Game name");

        titleBar.setTitle("Create new game");

        ConfirmDialog dialog = new ConfirmDialog(new VerticalLayout(gameName, maxDice));


        Button confirmButton = new UserSafeButton("Save", event -> {
            if (gameName.isValid()) {
                Game game = new Game();
                game.setName(gameName.getValue());
                game.setGameMasterId(sessionService.getLoggedInUser().getId());
                game.setMaxDice(maxDice.getValue());
                game = gameRepository.save(game);
                dialog.close();
                uiService.fireEvent(new AppEvent(AppEvent.AppEventType.MenuChange, game.getId()));
                UI.getCurrent().navigate("/gmsession/"+ game.getId());

            }
        });
        dialog.setConfirmButton(confirmButton);
        dialog.open();

        add(new UserSafeButton("Create game", event -> {dialog.open();}));

    }

}
