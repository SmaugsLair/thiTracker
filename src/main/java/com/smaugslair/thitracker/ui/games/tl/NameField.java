package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.Lumo;

public class NameField extends TextField implements ClickNotifier<TextField> {

    public NameField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super.setValue(item.getName());
        if (item.getColor()!=null && item.getColor().startsWith("DARK")) {
            getElement().setAttribute("theme", Lumo.DARK);
        }
        //setClassName(item.getColor());
        if (item.getPcId() == null) {
            addValueChangeListener(event -> {
                item.setName(getValue());
                gmTimeLineView.updateItem(item);
            });
        }
        else {
            addClickListener(event -> gmTimeLineView.showHeroDetails(item));
            setReadOnly(true);
        }
    }

}
