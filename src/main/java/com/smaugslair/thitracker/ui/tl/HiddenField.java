package com.smaugslair.thitracker.ui.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.GMTimeLineView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;

public class HiddenField extends Checkbox {

    public HiddenField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super.setValue(item.getHidden());
        addValueChangeListener(event -> {
            item.setHidden(getValue());
            gmTimeLineView.updateItem(item);
        });
    }

}
