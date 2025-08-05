package com.smaugslair.thitracker.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll()
@Route(value = "", layout = MainView.class)
public class WelcomeView extends HorizontalLayout {
    public WelcomeView() {

        setSizeFull();
        setSpacing(false);
        add("Welcome to the Hero Tracker");
    }
}
