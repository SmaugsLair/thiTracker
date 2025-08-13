package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.WelcomeView;
import com.smaugslair.thitracker.ui.components.AbstractMainView;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@PermitAll
@Route(value = "gamedeletion", layout = MainView.class)
@UIScope
public class GameDeletionView extends AbstractMainView implements HasUrlParameter<Long> {

    private Game game;

    private final PlayerCharacterRepository playerCharacterRepository;
    private final TimeLineItemRepository timeLineItemRepository;
    private final GameRepository gameRepository;
    private final UIService uiService;

    public GameDeletionView(TitleBar titleBar, PlayerCharacterRepository playerCharacterRepository, TimeLineItemRepository timeLineItemRepository, GameRepository gameRepository, UIService uiService) {
        super(titleBar);
        this.playerCharacterRepository = playerCharacterRepository;
        this.timeLineItemRepository = timeLineItemRepository;
        this.gameRepository = gameRepository;
        this.uiService = uiService;
        init();
    }

    public void init() {
        if (game == null) {
            return;
        }

        setTitle("Deletion of Game:"+game.getName());

        ConfirmDialog deleteDialog = new ConfirmDialog("Are you sure you want to delete the game "+game.getName()+"?");
        Button confirmButton = new UserSafeButton("Delete", event -> {
            List<PlayerCharacter> pcs = playerCharacterRepository.findAllByGameId(game.getId());
            pcs.forEach(pc -> pc.setGameId(null));
            playerCharacterRepository.saveAll(pcs);
            timeLineItemRepository.deleteAllByGameId(game.getId());
            gameRepository.delete(game);
            deleteDialog.close();

            uiService.fireEvent(new AppEvent(AppEvent.AppEventType.MenuChange, game.getId()));
            UI.getCurrent().navigate(WelcomeView.class);
            setTitle("");
        });
        deleteDialog.setConfirmButton(confirmButton);

        deleteDialog.open();

    }

    @Override
    public String getTitle() {
        return "Game Deletion";
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long gameId) {

        Optional<Game> oGame = gameRepository.findById(gameId);
        if (oGame.isPresent()) {
            game = oGame.get();
            uiService.setGame(game);
            init();
        }
    }
}
