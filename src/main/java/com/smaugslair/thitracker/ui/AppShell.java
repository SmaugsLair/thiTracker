package com.smaugslair.thitracker.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.ui.Transport;


@Push(transport = Transport.LONG_POLLING)
public class AppShell implements AppShellConfigurator {
}
