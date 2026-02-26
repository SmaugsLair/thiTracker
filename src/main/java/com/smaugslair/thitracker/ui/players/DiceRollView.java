package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.services.UIService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class DiceRollView extends HorizontalLayout {

    //private final static Logger log = LoggerFactory.getLogger(DiceRollView.class);

    public DiceRollView(UIService uiService) {
        setAlignItems(Alignment.START);

        DiceRoller diceRoller = new DiceRoller(uiService);
        add(new RollChooser(uiService.getPc(),  diceRoller));
        add(diceRoller);
    }
}
