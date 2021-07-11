package com.smaugslair.thitracker.ui.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;


public class ReactText extends IntegerField {

    public ReactText(TimeLineItem item) {
        setValue(item.getReactTime());
        setReadOnly(true);
    }
}
