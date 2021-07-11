package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.util.SessionService;
import com.smaugslair.thitracker.websockets.DiceRollBroadcaster;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TimeLineHistory extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(TimeLineHistory.class);

    private final SessionService sessionService;
    private Registration tlbReg;

    public TimeLineHistory(SessionService sessionService, EntryRepository entryRepository) {
        this.sessionService = sessionService;
        List<Entry> entryList = entryRepository.findAllByGameIdAndTypeEqualsOrderByIdDesc(
                sessionService.getGameId(), EventType.GMAction);
        for (Entry entry : entryList) {
            add(new Label(entry.getText()));
        }
    }
    public void addHistory(String text) {
        addComponentAsFirst(new Label(text));
    }


}
