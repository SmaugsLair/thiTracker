package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.function.Consumer;


public class FilterField extends TextField {

    public FilterField(Consumer<String> consumer, String width) {
        setValueChangeMode(ValueChangeMode.EAGER);
        if (width == null) {
            setSizeFull();
        }
        else {
            setWidth(width);
        }
        setPlaceholder("Filter");
        getElement().setAttribute("focus-target", "");
        addValueChangeListener(event -> {
            consumer.accept(event.getValue());
        });
    }

    public FilterField(Consumer<String> consumer) {
        this(consumer, null);
    }
}
