package com.smaugslair.thitracker.ui.users;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PermitAll
@Route(value = "userDetails", layout = MainView.class)
public class UserDetailsView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsView.class);
    private final SessionService sessionService;

    public UserDetailsView(SessionService sessionService) {
        this.sessionService = sessionService;
        init();
        sessionService.getTitleBar().setTitle("User Details");
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        User user = SecurityUtils.getLoggedInUser(sessionService);

        UserForm form = new UserForm(user);
        form.setWidth("500px");
        add(form);

        Button updateButton = new UserSafeButton("Apply changes", event -> {
            sessionService.getUserRepository().save(user);
            refresh();
        });
        add(updateButton);

    }
}
