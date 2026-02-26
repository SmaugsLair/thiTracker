package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.smaugslair.thitracker.ui.components.events.AppEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@UIScope
public class TimeLineHistory extends VerticalLayout implements AppEventListener {

    private static final Logger log = LoggerFactory.getLogger(TimeLineHistory.class);

    private final UIService uiService;
    private final EntryRepository entryRepository;

    public TimeLineHistory(UIService uiService, EntryRepository entryRepository) {
        this.uiService = uiService;
        this.entryRepository = entryRepository;
        uiService.addAppEventListener(this);
        setSpacing(false);
        setMargin(false);
    }

    public void init() {
        removeAll();

        Game game = uiService.getGame();

        if (game == null) {
            add(new Span("No Game loaded"));
            return;
        }
        List<Entry> entryList = entryRepository.findByGameId(game.getId())
                .stream().sorted().toList();
        for (Entry entry : entryList) {
            if (EventType.GMAction.equals(entry.getType())) {
                add(new Span(entry.getText()));
            }
        }
    }

    public void addHistory(Entry entry) {
        addComponentAsFirst(new Span(entry.getText()));
    }


    @Override
    public void onAppEvent(AppEvent e) {
        if (AppEvent.AppEventType.GameChosen.equals(e.getType())  ){
            init();
        }
    }
}
