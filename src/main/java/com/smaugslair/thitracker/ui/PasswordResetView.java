package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.PasswordReset;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.users.PasswordForm;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Route("resetpassword")
@PageTitle("Password Reset")
public class PasswordResetView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger log = LoggerFactory.getLogger(PasswordResetView.class);

	private final SessionService sessionService;

	public PasswordResetView(SessionService sessionService){
		this.sessionService = sessionService;
		setSizeUndefined();
		add(new H1("The Hero Instant"), new H3("Password reset"));

	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		List<String> resetTokens = beforeEnterEvent.getLocation().getQueryParameters().getParameters().get("resetToken");
		if (resetTokens != null) {
			String uuid = resetTokens.get(0);
			log.info("uuid: "+uuid);
			List<PasswordReset> passwordResets = sessionService.getPasswordResetRepo().findAll();
			AtomicBoolean found = new AtomicBoolean(false);
			passwordResets.forEach(passwordReset -> {
				if (passwordReset.isExpired()) {
					sessionService.getPasswordResetRepo().delete(passwordReset);
				}
				else if (SecurityUtils.matches(uuid, passwordReset.getHash())) {
					found.set(true);
					PasswordForm passwordForm = new PasswordForm();
					passwordForm.setWidth("400px");
					Button saveButton = new Button("Save new password", event -> {
						String validPassword = passwordForm.getValidPassword();
						if (validPassword != null) {
							Credentials credentials = sessionService.getCredRepo().findByUserId(passwordReset.getUserId());
							credentials.setEncodedPassword(SecurityUtils.encode(validPassword));
							sessionService.getCredRepo().save(credentials);
							UI.getCurrent().getPage().setLocation("logout");
							Notification.show("Password changed, please re-authenticate", 4000, Notification.Position.TOP_CENTER);
						}
					});
					add(passwordForm, saveButton);
				}
			});
			if (!found.get()) {
				UI.getCurrent().getPage().setLocation("logout");
			}
		}
	}


}