package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;

public class ConfirmDialog extends Dialog {
    //private final HorizontalLayout buttonRow;

    Button confirmButton;


    public ConfirmDialog() {
        getFooter().add(new UserSafeButton("Cancel", event -> close()));
    }

    public ConfirmDialog(String message) {
        this(new Span(message));
    }

    public ConfirmDialog(Component component) {
        //VerticalLayout verticalLayout = new VerticalLayout();
        //add(verticalLayout);
        add(component);
        //buttonRow = new HorizontalLayout();
        //.add(buttonRow);
        getFooter().add(new UserSafeButton("Cancel", event -> close()));
    }

    public void setConfirmButton(Button confirm) {
        getFooter().addComponentAsFirst(confirm);
        this.confirmButton = confirm;
    }

    public void enableConfirmButton(boolean enable) {
        confirmButton.setEnabled(enable);
    }
}
