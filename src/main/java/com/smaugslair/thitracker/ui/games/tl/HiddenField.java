package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.theme.lumo.Lumo;

public class HiddenField extends Checkbox {

    public HiddenField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super.setValue(item.getHidden());

        if (item.getColor()!=null && item.getColor().startsWith("DARK")) {
            getElement().setAttribute("theme", Lumo.DARK);
        }
        addValueChangeListener(event -> {
            item.setHidden(getValue());
            gmTimeLineView.updateItem(item);
        });
    }

}
