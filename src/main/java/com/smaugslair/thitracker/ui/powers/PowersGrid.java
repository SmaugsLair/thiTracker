package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;

import java.util.Collection;

public class PowersGrid extends Grid<Power> {

    public PowersGrid(Collection<Power> powers) {
        setThemeName("min-padding");
        setItems(powers);
        addColumn(new NativeButtonRenderer<>(
                item -> isDetailsVisible(item) ? "-" : "+",
                item -> setDetailsVisible(item, !isDetailsVisible(item))));
        addColumn(Power::getName).setHeader("Name");
        addColumn(Power::getShortDescr).setHeader("Short Description");
        addColumn(Power::getPrerequisite).setHeader("Prerequisites");
        addColumn(Power::getMaxTaken).setHeader("Limit");

        setItemDetailsRenderer(new ComponentRenderer<>(power -> new Paragraph(power.getFullDescr())));

        getColumns().forEach(powerColumn -> powerColumn.setAutoWidth(true));
        setWidthFull();
        setHeightByRows(true);

    }
}
