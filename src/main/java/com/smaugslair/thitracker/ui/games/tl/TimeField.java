package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.textfield.IntegerField;

public class TimeField extends IntegerField {
    private final TimeLineItem item;

    public TimeField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        this.item = item;
        super.setValue(item.getTime());
        setHasControls(true);
        setWidth(120, Unit.PIXELS);

        addValueChangeListener(event -> {
            item.setTime(getValue());
            gmTimeLineView.updateItem(item);
        });
    }

}
