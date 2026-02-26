package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.DiceHistory;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.sheet.CharacterSheet;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PermitAll
@PageTitle("Player Session")
@Route(value = "playersession", layout = MainView.class)
@RouteScope
public class PlayerSession extends AbstractHeroView {

    private static final Logger log = LoggerFactory.getLogger(PlayerSession.class);

    //private TabSheet tabSheet;

    private final PCTimeLineView pcTimeLineView;


    //private CharacterSheet characterSheet;

    protected PlayerSession(UIService uiService, GameRepository gameRepository, PlayerCharacterRepository playerCharacterRepository,
                            TitleBar titleBar, PCTimeLineView pcTimeLineView) {
        super(uiService, gameRepository, playerCharacterRepository, titleBar);
        this.pcTimeLineView = pcTimeLineView;

        setHeightFull();
        setWidthFull();
    }

    public void init() {
        removeAll();

        TabSheet tabSheet = new TabSheet();
        tabSheet.setHeightFull();
        tabSheet.setWidthFull();

        if (hero == null) {
            tabSheet.setPrefixComponent(new Span("PC not chosen"));
            return;
        }
        Game game = gameRepository.findById(hero.getGameId()).orElse(null);
        if (game == null) {
            tabSheet.setPrefixComponent(new Span("This pc is not in a game"));
            return;
        }
        uiService.setGame(game);

        titleBar.setTitle(hero.getName()+ " in " + game.getName());

        CharacterSheet characterSheet = new CharacterSheet();

        characterSheet.setPcUpdater(this::updatePc);
        //characterSheet.setEditablePowers(false);
        characterSheet.setPc(hero);
        characterSheet.setWidthFull();

        add(tabSheet);

        SplitLayout gameLayout = new SplitLayout();
        gameLayout.addToPrimary(pcTimeLineView);
        gameLayout.addToSecondary(new DiceHistory(uiService.getGame().getId()));
        gameLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        gameLayout.setSplitterPosition(50);

        tabSheet.add("Timeline", gameLayout);
        tabSheet.add("Dice Roller", new DiceRollView(uiService));
        tabSheet.add("Character Sheet", characterSheet);

    }

    public PlayerCharacter updatePc(PlayerCharacter pc) {
        log.info("updatePc");
        hero = playerCharacterRepository.save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(pc.getGameId());
        Broadcaster.broadcast(entry);
        return hero;
    }

}
