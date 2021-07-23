package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.vaadin.flow.component.textfield.IntegerField;


public class ReactText extends IntegerField {

    public ReactText(TimeLineItem item) {
        setValue(item.getReactTime());
        setReadOnly(true);
    }
}
