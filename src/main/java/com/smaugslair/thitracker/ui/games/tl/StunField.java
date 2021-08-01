package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.textfield.IntegerField;

public class StunField extends IntegerField {
    private final TimeLineItem item;

    public StunField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        this.item = item;
        super.setValue(item.getStun());
        setMin(0);
        setHasControls(true);
        setWidth(120, Unit.PIXELS);

        addValueChangeListener(event -> {
            item.setStun(getValue());
            gmTimeLineView.updateItem(item);
        });
    }

}
