package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.stream.Collectors;

public class InitiativeDialog extends Dialog {

    public InitiativeDialog(GMTimeLineView gmTimeLineView) {

        setModal(true);


        List<TimeLineItem> items = gmTimeLineView.getTliRepo().findByGameId(gmTimeLineView.getGameId()).stream()
                .sorted().collect(Collectors.toList());

        Grid<TimeLineItem> grid = new Grid<>();
        grid.setThemeName("min-padding");
        //grid.addThemeVariants(GridVariant.LUMO_COMPACT);

        grid.setAllRowsVisible(true);
        grid.setItems(items);



        grid.addComponentColumn(item -> new Span(item.getName())).setHeader("Character/NPC/Event")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(item -> new TimeField(item, gmTimeLineView)).setHeader("Time")
                .setAutoWidth(true).setFlexGrow(0);

        VerticalLayout vl = new VerticalLayout();
        add(vl);
        vl.add(grid);
        HorizontalLayout buttonRow = new HorizontalLayout();
        vl.add(buttonRow);
        buttonRow.add(new UserSafeButton("Close", event -> {
            close();
        }));
        //setWidthFull();
    }
}
