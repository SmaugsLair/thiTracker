package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.services.PasswordResetService;
import com.smaugslair.thitracker.ui.users.PasswordForm;
import com.smaugslair.thitracker.ui.users.UserForm;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.util.NameValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("login") 
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger log = LoggerFactory.getLogger(LoginView.class);

	private SessionService sessionService;

	private CacheService cacheService;

	private PasswordResetService passwordResetService;

	private LoginForm login = new LoginForm(); 

	public LoginView(){
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER); 
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("login");  

		add(new H1("The Hero Instant"), login);

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
				String validPassword = passwordForm.getValidPassword();
				if (validPassword != null) {
					user.setFriendCode(generateFriendCode());
					user = cacheService.getUserCache().save(user);
					Credentials credentials = new Credentials();
					credentials.setUserId(user.getId());
					credentials.setEncodedPassword(SecurityUtils.encode(validPassword));
					sessionService.getCredRepo().save(credentials);

					dialog.close();
					Notification.show("Account created, please login", 5000, Notification.Position.TOP_CENTER);
				}
				//userRepo.save(user);
			}
		});
		dialog.add(saveNewUser);

		Dialog forgotPasswordDialog = new Dialog();
		forgotPasswordDialog.setWidth("500px");
		forgotPasswordDialog.add(new Paragraph("An email will be sent to you with a link to reset your password. The link will expire after 5 minutes"));
		forgotPasswordDialog.add(new Span("Provide the email address for your account:"));
		TextField email = new TextField();
		email.setPlaceholder("email");
		forgotPasswordDialog.add(email);
		forgotPasswordDialog.add(new Button("Request reset", event -> {

			NameValue nameValue = new NameValue("email", email.getValue());
			Optional<User> user = cacheService.getUserCache().findOneByProperty(nameValue);
			if (user.isPresent()) {
				passwordResetService.createPasswordResetForUser(user.get());
			}
			else {
				log.info("no user found with email "+email.getValue());
			}
			forgotPasswordDialog.close();
			Notification.show("Check your inbox", 10000, Notification.Position.MIDDLE);
		}));

		login.addForgotPasswordListener(event -> {
			forgotPasswordDialog.open();
		});

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

	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@Autowired
	public void setPasswordResetService(PasswordResetService passwordResetService) {
		this.passwordResetService = passwordResetService;
	}

	@Autowired
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	private String generateFriendCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; ++i) {
			sb.append((int)(Math.random() * 10));
		}
		return sb.toString();

	}
}