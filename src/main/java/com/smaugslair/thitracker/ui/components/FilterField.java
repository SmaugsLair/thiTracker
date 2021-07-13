package com.smaugslair.thitracker.ui.components;

import com.smaugslair.thitracker.data.user.UserFilter;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;


public class FilterField extends TextField {

    public FilterField() {
        setValueChangeMode(ValueChangeMode.EAGER);
        setSizeFull();
        setPlaceholder("Filter");
        getElement().setAttribute("focus-target", "");
    }
}
