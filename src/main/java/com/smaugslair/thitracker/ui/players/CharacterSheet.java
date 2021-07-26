package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class CharacterSheet extends RegisteredVerticalLayout {

    private final SessionService sessionService;
    private final User user;

    public CharacterSheet(SessionService sessionService, User user) {
        this.sessionService = sessionService;
        this.user = user;
        init();
    }

    private void init() {
        removeAll();
        PlayerCharacter pc = sessionService.getPc();
        add(new Label(pc.getCharacterAndPlayerName(user)));
        add(new HorizontalLayout(new Label("Progression tokens"),
                new Span(pc.getProgressionTokens().toString())));
        add(new HorizontalLayout(new Label("Hero points available"),
                new Span(pc.getHeroPoints().toString())));
        add(new HorizontalLayout(new Label("Drama points spent"),
                new Span(pc.getDramaPoints().toString())));
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.PCUpdate.equals(entry.getType())) {
            if (entry.getPcId().equals(sessionService.getPc().getId())) {
                sessionService.refreshPc();
                init();
            }
        }
    }
}
