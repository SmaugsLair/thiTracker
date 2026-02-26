package com.smaugslair.thitracker.ui.games;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PairedComponent extends HorizontalLayout {

    private final static Logger log = LoggerFactory.getLogger(PairedComponent.class);

    public PairedComponent(Component first, Component second) {
        add(first);
        add(second);
        setAlignItems(Alignment.CENTER);
    }

    public PairedComponent(String first, String second) {
        add(first);
        if (second != null) {
            add(" : " + second);
        }
        setSpacing(false);
    }

    public PairedComponent(String displayName, int points) {
        VerticalLayout layout = new VerticalLayout();
        add(layout);
        IntegerField field = new IntegerField(displayName);
        field.setValue(points);
        field.setStepButtonsVisible(true);
        field.setMin(0);
        field.setMax(9);
        layout.add(field);
        //IntegerField size = new IntegerField();
        log.info("field size: {}", field.getWidth());
        log.info("field min w: {}", field.getMinWidth());
        /*size.setValue(Integer.valueOf(field.getWidth()));
        size.setStepButtonsVisible(true);
        size.addValueChangeListener(e -> {
            field.setW
        } );*/


    }
}
