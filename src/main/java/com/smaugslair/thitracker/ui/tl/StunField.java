package com.smaugslair.thitracker.ui.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.GMTimeLineView;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class StunField extends IntegerField {
    private final TimeLineItem item;

    public StunField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        this.item = item;
        super.setValue(item.getStun());
        setMin(0);
        setHasControls(true);

        addValueChangeListener(event -> {
            item.setStun(getValue());
            gmTimeLineView.updateItem(item);
        });
    }

}
