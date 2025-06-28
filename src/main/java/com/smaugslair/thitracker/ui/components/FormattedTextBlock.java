package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class FormattedTextBlock extends VerticalLayout {
    public FormattedTextBlock(String text, String label) {
        setSpacing(false);
        setMargin(false);
        setPadding(false);
        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER);
        TextArea textArea = new TextArea(label);
        textArea.setWidthFull();
        textArea.setHeightFull();
        textArea.setValue(text);
        textArea.setReadOnly(true);
        add(textArea);
    }
}
