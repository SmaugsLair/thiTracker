package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.theme.lumo.Lumo;

public class StunField extends IntegerField {

    public StunField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super.setValue(item.getStun());
        if (item.getColor()!=null && item.getColor().startsWith("DARK")) {
            getElement().setAttribute("theme", Lumo.DARK);
        }
        setMin(0);
        setStepButtonsVisible(true);
        setWidth(120, Unit.PIXELS);

        addValueChangeListener(event -> {
            item.setStun(getValue());
            gmTimeLineView.updateItem(item);
        });
    }

}
