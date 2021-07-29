package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;


public class TokenButton extends Icon {

    private final TimeLineItem item;

    private final GMTimeLineView gmTimeLineView;

    public TokenButton(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        super(VaadinIcon.COIN_PILES);
        setSize("16px");
        this.item = item;
        this.gmTimeLineView = gmTimeLineView;
        if (item.getPcId() == null) {
            setVisible(false);
            return;
        }
        addClickListener(event -> gmTimeLineView.showHeroDetails(item));
    }
}
