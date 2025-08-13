package com.smaugslair.thitracker.ui.users;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.AbstractMainView;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@PermitAll
@Route(value = "userDetails", layout = MainView.class)
@UIScope
@Component
public class UserDetailsView extends AbstractMainView {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsView.class);

    private final SessionService sessionService;
    private final UserRepository userRepository;

    public UserDetailsView(SessionService sessionService, UserRepository userRepository, TitleBar titleBar) {
        super(titleBar);
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        init();
    }


    private void refresh() {
        removeAll();
        init();
    }

    public void init() {


        User user = sessionService.getLoggedInUser();

        UserForm form = new UserForm(user);
        form.setWidth("500px");
        add(form);

        Button updateButton = new UserSafeButton("Apply changes", event -> {
            userRepository.save(user);
            refresh();
        });
        add(updateButton);

    }

    @Override
    public String getTitle() {
        return "User Details";
    }
}
