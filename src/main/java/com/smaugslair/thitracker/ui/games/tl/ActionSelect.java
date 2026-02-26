package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.atd.ActionTime;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.theme.lumo.Lumo;

public class ActionSelect extends Select<ActionTime> {

    public static final ActionTime unselectedActionTime = new ActionTime("Choose action:");

    public ActionSelect(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        setItems(item.getActionTimes());
        setValue(unselectedActionTime);

        if (item.getColor()!=null && item.getColor().startsWith("DARK")) {
            getElement().setAttribute("theme", Lumo.DARK);
        }
        addValueChangeListener(event -> {
            item.setActionTime(getValue());
            gmTimeLineView.updateItem(item);
        });

    }
}
