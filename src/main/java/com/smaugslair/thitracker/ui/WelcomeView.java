package com.smaugslair.thitracker.ui;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll()
@Route(value = "", layout = MainView.class)
public class WelcomeView extends HorizontalLayout {
    public WelcomeView() {

        setSizeFull();
        setSpacing(true);
        setPadding(true);
        setVerticalComponentAlignment(Alignment.CENTER);
        //setAlignItems(Alignment.CENTER);
        add(new H3("Welcome to the Hero Tracker"));
    }
}
