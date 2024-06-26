package com.smaugslair.thitracker.ui.users;

import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
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

        User user = SecurityUtils.getLoggedInUser();

        UserForm form = new UserForm(user);
        form.setWidth("500px");
        add(form);

        Button updateButton = new UserSafeButton("Apply changes", event -> {
            sessionService.getUserRepository().save(user);
            refresh();
        });
        add(updateButton);

        Dialog dialog = new Dialog();
        PasswordForm passwordForm = new PasswordForm();
        dialog.add(passwordForm);
        dialog.setWidth("500px");
        Button savePassword = new UserSafeButton("Save password", event -> {
            String validPassword = passwordForm.getValidPassword();
            if (validPassword != null) {
                Credentials credentials = sessionService.getCredRepo().findByUserId(user.getId());
                credentials.setEncodedPassword(SecurityUtils.encode(validPassword));
                sessionService.getCredRepo().save(credentials);
                dialog.close();
                UI.getCurrent().getPage().setLocation("logout");
                Notification.show("Password changed, please re-authenticate", 4000, Notification.Position.TOP_CENTER);
            }
        });
        dialog.add(savePassword);

        Button updatePassword = new UserSafeButton("Update password", event -> dialog.open());
        add(updatePassword);
    }
}
