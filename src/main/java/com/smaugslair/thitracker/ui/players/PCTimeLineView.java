package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.smaugslair.thitracker.ui.components.events.AppEventListener;
import com.smaugslair.thitracker.util.BeanFinder;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PermitAll
@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
@Route(value = "pcTimeLineView")
@Component
@RouteScope
public class PCTimeLineView extends RegisteredVerticalLayout implements AppEventListener, HasUrlParameter<Long> {

    //private static final Logger log = LoggerFactory.getLogger(PCTimeLineView.class);

    private final UIService uiService;
    private final TimeLineItemRepository timeLineItemRepository;

    private Game game = null;

    public PCTimeLineView(UIService uiService, TimeLineItemRepository timeLineItemRepository) {
        this.uiService = uiService;
        this.timeLineItemRepository = timeLineItemRepository;

        uiService.addAppEventListener(this);
        //log.info("PCTimeLineView created");
    }

    public void init() {
        //log.info("PCTimeLineView init");
        removeAll();

        //final Game game = uiService.getGame();
        if (game == null || game.getId() == null) {
            add(new H1("Game not found"));
            return;
        }

        List<TimeLineItem> items = timeLineItemRepository.findByGameId(game.getId())
                .stream().filter(item -> !item.getHidden()).sorted().collect(Collectors.toList());


        TimeLineItem lastEvent = null;
        if (game.getLastEventId() != null) {
            lastEvent = timeLineItemRepository.findById(game.getLastEventId()).orElse(null);
        }

        for (TimeLineItem item : items) {
            if (lastEvent != null) {
                item.setReactTime(lastEvent.getTime() - item.getTime());
            }
        }

        Grid<TimeLineItem> grid = new Grid<>();
        grid.setAllRowsVisible(true);
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
        if (EventType.GMAction.equals(entry.getType()) && game != null) {
            if (entry.getGameId().equals(game.getId())) {
                //log.info("PCTimeLineView handleMessage");
                init();
            }
        }
    }

    @Override
    public void onAppEvent(AppEvent e) {
        if (AppEvent.AppEventType.GameChosen.equals(e.getType())) {
            //log.info("PCTimeLineView onAppEvent");
            game = uiService.getGame();
            init();
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long gameId) {
        Optional<Game> optionalGame = BeanFinder.getBean(GameRepository.class).findById(gameId);
        if (optionalGame.isPresent()) {
            game = optionalGame.get();
        }
        else {
            game = null;
        }
        init();
    }
}
