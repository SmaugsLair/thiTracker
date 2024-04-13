package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.security.SecurityUtils;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PermitAll
@Route("logout")
@PageTitle("Logout")
public class LogoutView extends VerticalLayout {

	private static final Logger log = LoggerFactory.getLogger(LogoutView.class);

	public LogoutView() {
		setSizeFull();
		setAlignItems(Alignment.CENTER); 
		setJustifyContentMode(JustifyContentMode.CENTER);
		SecurityUtils.logout();
		add(new Span("User logged out"));
		add(new RouterLink("Login", LoginView.class));
	}


}