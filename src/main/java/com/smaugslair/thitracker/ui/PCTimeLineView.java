package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.util.SessionService;
import com.smaugslair.thitracker.websockets.TimelineBroadcaster;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
public class PCTimeLineView extends VerticalLayout {

    private static Logger log = LoggerFactory.getLogger(PCTimeLineView.class);

    private final CollectedItemRepository ciRepo;
    private final GameRepository gameRepo;
    private final TimeLineItemRepository tliRepo;
    private final PlayerCharacterRepository pcRepo;
    private final SessionService sessionService;



    public PCTimeLineView(GameRepository gameRepository,
                          TimeLineItemRepository tliRepository,
                          CollectedItemRepository collectedItemRepository,
                          PlayerCharacterRepository playerCharacterRepository,
                          SessionService sessionService) {
        gameRepo = gameRepository;
        tliRepo = tliRepository;
        ciRepo = collectedItemRepository;
        pcRepo = playerCharacterRepository;
        this.sessionService = sessionService;

        init();

    }

    public void init() {

        if (sessionService.getGameId() == null) {
            add(new H1("No game specified"));
            return;
        }

        final Game game = gameRepo.findById(sessionService.getGameId()).orElse(new Game());
        if (game.getId() == null) {
            add(new H1("Game not found"));
            return;
        }

        add( new H3("Game: "+game.getName()));

        List<TimeLineItem> items = tliRepo.findByGameIdAndHiddenFalseOrderByTime(game.getId());

        TimeLineItem lastEvent = null;
        if (game.getLastEventId() != null) {
            lastEvent = tliRepo.findById(game.getLastEventId()).orElse(null);
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
        tlbReg = TimelineBroadcaster.register(newMessage -> {
            ui.access(() -> {
                if (newMessage.getGameId().equals(sessionService.getGameId())) {
                    //log.info("accepting:"+newMessage);
                    removeAll();
                    init();
                }
                else {
                    //log.info("ignoring:"+newMessage);
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
