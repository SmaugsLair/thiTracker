package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.textfield.IntegerField;

public class InitTimeField extends IntegerField {

    public InitTimeField(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super.setValue(item.getTime());
        setStepButtonsVisible(true);

        addValueChangeListener(event -> {
            item.setTime(getValue());
            gmTimeLineView.updateItem(item);
        });
    }

}
