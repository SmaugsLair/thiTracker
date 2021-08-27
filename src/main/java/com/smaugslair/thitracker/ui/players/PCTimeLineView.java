package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class PCTimeLineView extends RegisteredVerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(PCTimeLineView.class);

    private final SessionService sessionService;

    public PCTimeLineView(SessionService sessionService) {
        this.sessionService = sessionService;
        init();
    }

    public void init() {

        Long gameId = sessionService.getGameId();

        if (gameId == null) {
            add(new H1("No Game loaded"));
            return;
        }

        final Game game = sessionService.getGameRepo().findById(gameId).orElse(new Game());
        if (game.getId() == null) {
            add(new H1("Game not found"));
            return;
        }

        User gm = sessionService.getUserRepository().findById(game.getGameMasterId()).orElse(new User());

        add( new H3("Game: "+game.getName()+ " by "+ gm.getDisplayName()));

        List<TimeLineItem> items = sessionService.getTliRepo().findByGameId(gameId)
                .stream().filter(item -> !item.getHidden()).sorted().collect(Collectors.toList());


        TimeLineItem lastEvent = null;
        if (game.getLastEventId() != null) {
            lastEvent = sessionService.getTliRepo().findById(game.getLastEventId()).orElse(null);
        }

        for (TimeLineItem item : items) {
            if (lastEvent != null) {
                item.setReactTime(lastEvent.getTime() - item.getTime());
            }
        }

        Grid<TimeLineItem> grid = new Grid<>();
        grid.setHeightByRows(true);
        grid.setItems(items);

        grid.addColumn(TimeLineItem::getName).setHeader("Character/NPC/Event");
        grid.addColumn(TimeLineItem::getStun).setHeader("Stun");
        grid.addColumn(TimeLineItem::getTime).setHeader("Time");
        grid.addColumn(TimeLineItem::getReactTime).setHeader("React");

        grid.setClassNameGenerator(item -> item.getColor());
        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));

        add(grid);
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.GMAction.equals(entry.getType())) {
            if (entry.getGameId().equals(sessionService.getGameId())) {
                removeAll();
                init();
            }
        }
    }
}
