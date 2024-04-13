package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TimeLineHistory extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(TimeLineHistory.class);

    public TimeLineHistory(GMTimeLineView gmTimeLineView) {

        setSpacing(false);
        setMargin(false);

        if (gmTimeLineView.getGameID() == null) {
            add(new Span("No Game loaded"));
            return;
        }
        List<Entry> entryList = gmTimeLineView.getEntryRepo().findByGameId(gmTimeLineView.getGameID())
                .stream().sorted().collect(Collectors.toList());
        for (Entry entry : entryList) {
            if (EventType.GMAction.equals(entry.getType())) {
                add(new Span(entry.getText()));
            }
        }
    }

    public void addHistory(Entry entry) {
        addComponentAsFirst(new Span(entry.getText()));
    }


}
