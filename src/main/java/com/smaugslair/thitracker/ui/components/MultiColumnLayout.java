package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MultiColumnLayout extends HorizontalLayout {

    private final int columnCount;
    private int currentColumn = 0;

    public MultiColumnLayout(int columnCount) {
        if (columnCount < 2) {
            throw new IllegalArgumentException("ColumnCount must be 2 or more");
        }
        this.columnCount = columnCount;
        for (int i = 0; i < columnCount; ++i) {
            add(new VerticalLayout());
        }
    }

    public void addToNextColumn(Component component) {
        ((VerticalLayout)getComponentAt(currentColumn)).add(component);
        ++currentColumn;
        if (currentColumn >= columnCount) {
            currentColumn = 0;
        }
    }
}
