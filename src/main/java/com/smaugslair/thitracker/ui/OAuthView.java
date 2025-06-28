package com.smaugslair.thitracker.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Hero Tracker Login")
@AnonymousAllowed
public class OAuthView extends VerticalLayout {
    // URL that Spring Security uses to connect to Google services
    private static final String OAUTH_URL = "/oauth2/authorization/google";

    public OAuthView() {
        setClassName("login");
        setSizeFull();
        Button button = new Button("Login with Google", e -> {
            UI.getCurrent().getPage().open(OAUTH_URL, "_self");
        });
        button.setClassName("login-button");
        add(button);
    }
}