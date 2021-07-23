package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
public class PCTimeLineView extends VerticalLayout {

    private static Logger log = LoggerFactory.getLogger(PCTimeLineView.class);

    private final SessionService sessionService;

    public PCTimeLineView(SessionService sessionService) {
        this.sessionService = sessionService;

        init();

    }

    public void init() {

        Long gameId = sessionService.getGameId();

        if (gameId == null) {
            add(new H1("No game specified"));
            return;
        }

        final Game game = sessionService.getGameRepo().findById(gameId).orElse(new Game());
        if (game.getId() == null) {
            add(new H1("Game not found"));
            return;
        }

        User gm = sessionService.getUserRepo().findById(game.getGameMasterId()).get();

        add( new H3("Game: "+game.getName()+ " by "+ gm.getDisplayName()));

        List<TimeLineItem> items = sessionService.getTliRepo().findByGameIdAndHiddenFalseOrderByTime(game.getId());

        TimeLineItem lastEvent = null;
        if (game.getLastEventId() != null) {
            lastEvent = sessionService.getTliRepo().findById(game.getLastEventId()).orElse(null);
        }

        for (TimeLineItem item : items) {
            if (lastEvent != null) {
                item.setReactTime(lastEvent.getTime() - item.getTime());
            }
            /*
            item.getActionTimes().add(ActionSelect.unselectedActionTime);
            for (ActionTimeDefault atd: AtdCache.getAtds()) {
                item.getActionTimes().add(
                        new ActionTime(atd.getName(),
                                atd.getTime() + item.getStun()
                                        + item.getDeltas().get(atd.getName()).getDelta()));
            }*/
        }

        Grid<TimeLineItem> grid = new Grid<>();
        grid.setHeightByRows(true);
        grid.setItems(items);

        grid.addColumn(TimeLineItem::getName).setHeader("Character/NPC/Event");
        grid.addColumn(TimeLineItem::getStun).setHeader("Stun");
        grid.addColumn(TimeLineItem::getTime).setHeader("Time");
        grid.addColumn(TimeLineItem::getReactTime).setHeader("React");

        grid.setClassNameGenerator(item -> "w3-"+item.getColor());
        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));

        add(grid);
    }


    private Registration tlbReg;
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        tlbReg = Broadcaster.register(newMessage -> {
            ui.access(() -> {
                if (EventType.GMAction.equals(newMessage.getType())) {
                    if (newMessage.getGameId().equals(sessionService.getGameId())) {
                        removeAll();
                        init();
                    }
                }
            });
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        tlbReg.remove();
        tlbReg = null;
    }
}
