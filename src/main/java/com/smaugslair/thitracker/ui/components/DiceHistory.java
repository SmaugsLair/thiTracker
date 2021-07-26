package com.smaugslair.thitracker.ui.components;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.util.NameValue;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DiceHistory extends RegisteredVerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(DiceHistory.class);

    private final SessionService sessionService;

    public DiceHistory(SessionService sessionService, CacheService cacheService) {
        setMargin(false);
        setSpacing(false);
        this.sessionService = sessionService;
        NameValue nameValue = new NameValue("gameId", sessionService.getGameId());
        List<Entry> entryList = cacheService.getEntryCache().findManyByProperty(nameValue)
                .stream().sorted().collect(Collectors.toList());
        for (Entry entry : entryList) {
            if (entry.getType().equals(EventType.DiceRoll)) {
                add(new Label(entry.getText()));
            }
        }
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (entry.getGameId().equals(sessionService.getGameId())) {
            //log.info(entry.toString());
            switch (entry.getType()) {
                case DiceRoll:
                    addComponentAsFirst(new Label(entry.getText()));
                    break;
                case ClearRolls:
                    removeAll();
                    break;
            }
        }

    }
}
