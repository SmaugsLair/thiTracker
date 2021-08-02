package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.util.NameValue;
import com.vaadin.flow.component.html.Label;
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

        NameValue nameValue = new NameValue("gameId", gmTimeLineView.getGameID());
        if (nameValue.getValue() == null) {
            add(new Label("No Game selected"));
            return;
        }
        List<Entry> entryList = gmTimeLineView.getEntryCache().findManyByProperty(nameValue)
                .stream().sorted().collect(Collectors.toList());
        for (Entry entry : entryList) {
            if (EventType.GMAction.equals(entry.getType())) {
                add(new Label(entry.getText()));
            }
        }
    }

    public void addHistory(Entry entry) {
        addComponentAsFirst(new Label(entry.getText()));
    }


}
