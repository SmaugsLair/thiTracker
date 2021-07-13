package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDialog extends Dialog {
    private final HorizontalLayout buttonRow;

    public ConfirmDialog(String message) {
        this(new Text(message));
    }

    public ConfirmDialog(Component component) {
        VerticalLayout verticalLayout = new VerticalLayout();
        add(verticalLayout);
        verticalLayout.add(component);
        buttonRow = new HorizontalLayout();
        verticalLayout.add(buttonRow);
        buttonRow.add(new Button("Cancel", event -> close()));
    }

    public void setConfirmButton(Button confirm) {
        buttonRow.addComponentAsFirst(confirm);
    }
}
