package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.textfield.TextField;

public class NameField extends TextField {

    public NameField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super.setValue(item.getName());
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
