package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.smaugslair.thitracker.util.ColorCollection;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class ColorDialog extends Dialog {


    private final GMTimeLineView gmTimeLineView;
    private TimeLineItem item;

    public ColorDialog(GMTimeLineView gmTimeLineView) {
        this.gmTimeLineView = gmTimeLineView;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        //List<Grid<ColorCollection.Color>> grids = new ArrayList<>(5);
        for (int i = 0 ; i < 5 ; ++i) {
            Grid<ColorCollection.Color> grid = new Grid<>();
            grid.setThemeName("min-padding");
            //grids.add(grid);
            horizontalLayout.add(grid);

            grid.setItems(ColorCollection.colorLists.get(i));
            //grid.addColumn(ColorCollection.Color::getName);
            grid.addComponentColumn(ColorButton::new);

            grid.setClassNameGenerator(item -> item.getName());
        }
        add(horizontalLayout);
        setWidthFull();
    }

    public void openWith(TimeLineItem item) {
        this.item = item;
        open();
    }

    private class ColorButton extends Div {

        ColorButton(ColorCollection.Color color) {
            setText(color.getName());
            addClickListener(event -> {
                item.setColor(color.getName());
                gmTimeLineView.updateItem(item);
                close();
            });
        }
    }
}
