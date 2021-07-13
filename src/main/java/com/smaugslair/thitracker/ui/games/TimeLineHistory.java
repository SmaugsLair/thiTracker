package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TimeLineHistory extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(TimeLineHistory.class);

    public TimeLineHistory(GMTimeLineView gmTimeLineView) {

        List<Entry> entryList = gmTimeLineView.getEntryRepo().findAllByGameIdAndTypeEqualsOrderByIdDesc(
                gmTimeLineView.getGameID(), EventType.GMAction);
        for (Entry entry : entryList) {
            add(new Label(entry.getText()));
        }
    }

    public void addHistory(Entry entry) {
        addComponentAsFirst(new Label(entry.getText()));
    }


}
