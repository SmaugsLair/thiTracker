package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.smaugslair.thitracker.util.ColorCollection;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
public class ColorDialog extends Dialog {


    private final GMTimeLineView gmTimeLineView;
    private Optional<TimeLineItem> item;

    public ColorDialog(GMTimeLineView gmTimeLineView) {
        this.gmTimeLineView = gmTimeLineView;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<Grid<ColorCollection.Color>> grids = new ArrayList<>(5);
        for (int i = 0 ; i < 5 ; ++i) {
            Grid<ColorCollection.Color> grid = new Grid<>();
            grids.add(grid);
            horizontalLayout.add(grid);

            grid.setItems(ColorCollection.colorLists.get(i));
            //grid.addColumn(ColorCollection.Color::getName);
            grid.addComponentColumn(ColorButton::new);

            grid.setClassNameGenerator(item -> "w3-"+item.getName());
        }
        add(horizontalLayout);
        setWidthFull();
    }

    public void openWith(Optional<TimeLineItem> item) {
        this.item = item;
        open();
    }

    private class ColorButton extends Div {

        ColorButton(ColorCollection.Color color) {
            setText(color.getName());
            addClickListener(event -> {
                if (item.isPresent()) {
                    item.get().setColor(color.getName());
                    gmTimeLineView.updateItem(item.get());
                    close();
                }

            });
        }
    }
}