package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Friends Page")
@Route(value = "friends", layout = MainView.class)
public class FriendsSession extends SplitLayout {
    private final SessionService sessionService;

    public FriendsSession(SessionService sessionService) {
        this.sessionService = sessionService;
        init();
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
