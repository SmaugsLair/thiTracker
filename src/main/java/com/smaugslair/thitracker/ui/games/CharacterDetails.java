package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.games.tl.TraitPoints;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterDetails extends RegisteredVerticalLayout {

    private final static Logger log = LoggerFactory.getLogger(CharacterDetails.class);

    private final SessionService sessionService;
    private final GMTimeLineView gmTimeLineView;
    PlayerCharacter pc = null;
    User user = null;

    public CharacterDetails(SessionService sessionService, GMTimeLineView gmTimeLineView) {
        this.sessionService = sessionService;
        this.gmTimeLineView = gmTimeLineView;
        setPadding(false);
        setSpacing(false);
        init();
    }

    public void setPc(PlayerCharacter pc, User user) {
        this.pc = pc;
        this.user = user;
        removeAll();
        init();
    }

    private void init() {
        if (pc == null) {
            add(new Paragraph("Choose a Hero to view details"));
            return;
        }

        IntegerField progression = new IntegerField();
        progression.setValue(pc.getProgressionTokens());
        progression.setMin(0);
        progression.setHasControls(true);
        progression.addValueChangeListener(event -> {
            pc.setProgressionTokens(event.getValue());
            updatePC();
        });

        add(new Label(pc.getCharacterAndPlayerName(user)));
        add(new HorizontalLayout(new Label("Progression tokens"), progression));

        Grid<Trait> grid = new Grid<>();
        grid.setHeightByRows(true);
        grid.setItems(pc.getTraits().stream().sorted());

        grid.addColumn(Trait::getType);
        grid.addColumn(Trait::getName);
        grid.addComponentColumn(trait -> new TraitPoints(trait, this));

        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        add(grid);
    }

    public void updatePC() {
        gmTimeLineView.updatePc(pc);
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.PlayerPCUpdate.equals(entry.getType())) {
            if (pc != null && entry.getPcId().equals(pc.getId())) {
                removeAll();
                init();
            }
        }
    }
}
