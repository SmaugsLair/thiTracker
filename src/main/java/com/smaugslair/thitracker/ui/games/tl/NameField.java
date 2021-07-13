package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NameField extends TextField {

    private static Logger log = LoggerFactory.getLogger(NameField.class);
    private TimeLineItem item;
    private final GMTimeLineView gmTimeLineView;

    public NameField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super.setValue(item.getName());
        this.item = item;
        this.gmTimeLineView = gmTimeLineView;
        if (item.getPcId() == null) {
            addValueChangeListener(event -> {
                item.setName(getValue());
                gmTimeLineView.updateItem(item);
            });
        }
        else {
            setReadOnly(true);
        }
    }

}
