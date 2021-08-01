package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.games.tl.ProgressionPoint;
import com.smaugslair.thitracker.ui.games.tl.TraitPoint;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        add(new Label(pc.getCharacterAndPlayerName(user)));

        List<TraitRow> traitRows = new ArrayList<>(9);
        traitRows.add(new TraitRow(new ProgressionPoint(pc, this)));

        List<Trait> traits = pc.getTraits().stream().sorted().collect(Collectors.toList());

        traitRows.add(new TraitRow("Hero Traits"));
        traits.forEach(trait -> {
            if (trait.getType().equals(TraitType.Hero)) {
                traitRows.add(new TraitRow(new TraitPoint(trait, this)));
            }
        });

        traitRows.add(new TraitRow("Drama Traits"));
        traits.forEach(trait -> {
            if (trait.getType().equals(TraitType.Drama)) {
                traitRows.add(new TraitRow(new TraitPoint(trait, this)));
            }
        });

        Grid<TraitRow> grid = new Grid<>();
        grid.setHeightByRows(true);
        grid.setItems(traitRows);

        grid.addColumn(TraitRow::getName).setFlexGrow(2);
        grid.addComponentColumn(TraitRow::getComponent).setFlexGrow(1);

        grid.setClassNameGenerator(item -> item.isSubHead() ? "w3-light-grey" : "");

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
