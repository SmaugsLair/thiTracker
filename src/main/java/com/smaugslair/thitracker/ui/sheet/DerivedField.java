package com.smaugslair.thitracker.ui.sheet;

import com.vaadin.flow.component.html.Span;

public class DerivedField {

    private final String name;

    private final Span span = new Span();


    public DerivedField(String name, String value) {
        this.name = name;
        span.setText(value);
    }

    public String getName() {
        return name;
    }

    public Span getSpan() {
        return span;
    }
}
