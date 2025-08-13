package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

//@VaadinSessionScope
@UIScope
@org.springframework.stereotype.Component
public class TitleBar extends VerticalLayout  {

    public void setTitle(String title) {
        removeAll();
        add(new H2(title));
    }

    public void setComponent(Component comp) {
        removeAll();
        add(comp);
    }

}
