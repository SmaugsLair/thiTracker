package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.CredentialsRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("login") 
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger log = LoggerFactory.getLogger(LoginView.class);

	private final UserRepository userRepo;
	private final CredentialsRepository credRepo;
	private LoginForm login = new LoginForm(); 

	public LoginView(UserRepository userRepository, CredentialsRepository credentialsRepository){
		userRepo = userRepository;
		credRepo = credentialsRepository;
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER); 
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("login");  

		add(new H1("THI"), login);

		Dialog dialog = new Dialog();
		dialog.setWidth("500px");
		UserForm userForm = new UserForm(new User());
		dialog.add(userForm);
		PasswordForm passwordForm = new PasswordForm();
		dialog.add(passwordForm);
		Button saveNewUser = new Button("Save new user");
		saveNewUser.addClickListener(event -> {
			User user = userForm.getUser();
			if (user != null) {
				log.info("user captured: " +user );
				String validPassword = passwordForm.getValidPassword();
				log.info("validPassword: " +validPassword );
				if (validPassword != null) {
					user = userRepo.save(user);
					Credentials credentials = new Credentials();
					credentials.setUserId(user.getId());
					credentials.setEncodedPassword(SecurityUtils.encode(validPassword));
					log.info("password captured: " +credentials );
					credRepo.save(credentials);

					dialog.close();
					Notification.show("Account created, please login", 5000, Notification.Position.TOP_CENTER);
				}
				//userRepo.save(user);
			}
		});
		dialog.add(saveNewUser);

		add(new Button("Create new account", event -> dialog.open()));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation() 
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(true);
        }
	}
}