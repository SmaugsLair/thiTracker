package com.smaugslair.thitracker.ui.sheet;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;

import java.util.Collection;

public class PowerTargetGrid extends Grid<PowerTarget> {

    private int available;
    private final Integer tier;

    public PowerTargetGrid(Integer tier, Collection<PowerTarget> powers, PowerSetEditor powerSetEditor) {
        this.tier = tier;
        powers.forEach(powerTarget -> {
            if (powerTarget.isAvailable()) {
                ++available;
            }
        });
        setThemeName("min-padding");
        setItems(powers);
        addColumn(PowerTarget::getTakenText).setHeader("Taken");
        addComponentColumn(item -> {
                    if (item.isAvailable()) {
                        return new PowerTargetAddButton(item, powerSetEditor);
                    }
                    else return new Label("");
                }
        );
        addComponentColumn(item -> {
                    if (item.getTimesTaken() > 0) {
                        return new PowerTargetRemoveButton(item, powerSetEditor);
                    }
                    else return new Label("");
                }
        );
        addColumn(PowerTarget::getName).setHeader("Name");
        addColumn(PowerTarget::getShortDescr).setHeader("Short Description");
        addColumn(PowerTarget::getPrerequisite).setHeader("Prerequisites");
        addColumn(new NativeButtonRenderer<>(
                item -> isDetailsVisible(item) ? "^" : "v",
                item -> setDetailsVisible(item, !isDetailsVisible(item))));

        setItemDetailsRenderer(new ComponentRenderer<>(power -> new Paragraph(power.getFullDescr())));

        getColumns().forEach(powerColumn -> powerColumn.setAutoWidth(true));
        setWidthFull();
        setHeightByRows(true);

    }

    public int getAvailable() {
        return available;
    }

    public Integer getTier() {
        return tier;
    }

}
