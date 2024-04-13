package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Friends Page")
@Route(value = "friends", layout = MainView.class)
public class FriendsView extends SplitLayout {
    private final SessionService sessionService;

    public FriendsView(SessionService sessionService) {
        this.sessionService = sessionService;
        init();
        sessionService.getTitleBar().setTitle("Friends");
    }

    public void refresh() {
        removeAll();
        init();
    }

    private void init() {
        addToPrimary(new FriendsList(sessionService));
        addToSecondary(new FriendFinder(this, sessionService));
    }




}
