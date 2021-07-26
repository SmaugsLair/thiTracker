package com.smaugslair.thitracker.ui.users;

import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Route(value = "userDetails", layout = MainView.class)
public class UserDetailsView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsView.class);
    private final SessionService sessionService;
    private final CacheService cacheService;

    public UserDetailsView(SessionService sessionService, CacheService cacheService) {
        this.sessionService = sessionService;
        this.cacheService = cacheService;

        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        add(new H3("User Details"));
        User user = SecurityUtils.getLoggedInUser();

        UserForm form = new UserForm(user);
        form.setWidth("500px");
        add(form);

        Button updateButton = new Button("Update", event -> {
            cacheService.getUserCache().save(user);
            refresh();
        });
        add(updateButton);

        Dialog dialog = new Dialog();
        PasswordForm passwordForm = new PasswordForm();
        dialog.add(passwordForm);
        dialog.setWidth("500px");
        Button savePassword = new Button("Save password", event -> {
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

        Button updatePassword = new Button("Update password", event -> dialog.open());
        add(updatePassword);
    }
}
