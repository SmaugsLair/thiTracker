package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@PermitAll
@Route("logout")
@PageTitle("Logout")
@UIScope
public class LogoutView extends VerticalLayout {

	private static final Logger log = LoggerFactory.getLogger(LogoutView.class);

	private final SessionService sessionService;

	public LogoutView(SessionService sessionService) {
        this.sessionService = sessionService;
        setSizeFull();
		setAlignItems(Alignment.CENTER); 
		setJustifyContentMode(JustifyContentMode.CENTER);
	}

	@PostConstruct
	public void init() {
		String email = sessionService.getUser().getEmail();
		log.info("Logging out user: " + email);
		sessionService.setUser(null);
		sessionService.logout();
		add(new Span("User logged out"));
		add(new RouterLink("Login", OAuthView.class));
		UI.getCurrent().getSession().close();
	}


}