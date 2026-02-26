package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.theme.lumo.Lumo;


public class ReactText extends IntegerField {

    public ReactText(TimeLineItem item) {
        setWidth("50px");

        if (item.getColor()!=null && item.getColor().startsWith("DARK")) {
            getElement().setAttribute("theme", Lumo.DARK);
        }
        setValue(item.getReactTime());
        setReadOnly(true);
    }
}
