package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class CharacterSheet extends RegisteredVerticalLayout {

    private final SessionService sessionService;
    private final User user;

    public CharacterSheet(SessionService sessionService, User user) {
        this.sessionService = sessionService;
        this.user = user;
        setPadding(false);
        setSpacing(false);
        init();
    }

    private void init() {
        removeAll();
        PlayerCharacter pc = sessionService.getPc();
        add(new Label(pc.getCharacterAndPlayerName(user)));
        add(new HorizontalLayout(new Label("Progression tokens"),
                new Span(pc.getProgressionTokens().toString())));

        Grid<Trait> grid = new Grid<>();
        grid.setHeightByRows(true);
        grid.setItems(pc.getTraits().stream().sorted());

        grid.addColumn(Trait::getType);
        grid.addColumn(Trait::getName);
        grid.addColumn(Trait::getPoints);

        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        add(grid);

    }

    @Override
    protected void handleMessage(Entry entry) {
        switch (entry.getType()) {
            case GMPCUpdate:
            case PlayerPCUpdate:
                if (entry.getPcId().equals(sessionService.getPc().getId())) {
                    sessionService.refreshPc();
                    init();
                }
                break;
        }
    }

}
