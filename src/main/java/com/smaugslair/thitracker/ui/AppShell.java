package com.smaugslair.thitracker.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.theme.Theme;


@Push(transport = Transport.LONG_POLLING)
@Theme("thi")
@PWA(name = "Hero Tracker for the Hero Instant", shortName = "THI")
public class AppShell implements AppShellConfigurator {
}
