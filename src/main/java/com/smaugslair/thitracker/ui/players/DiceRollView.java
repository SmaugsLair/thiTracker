package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DiceRollView extends HorizontalLayout {


    public DiceRollView(SessionService sessionService) {
        DiceRoller diceRoller = new DiceRoller(sessionService);
        add(new RollChooser(sessionService, diceRoller));
        add(diceRoller);
        setAlignItems(Alignment.START);
    }
}
