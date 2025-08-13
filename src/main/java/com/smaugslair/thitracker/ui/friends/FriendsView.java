package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.AbstractMainView;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

@Component
@PermitAll
@PageTitle("Friends Page")
@Route(value = "friends", layout = MainView.class)
@UIScope
public class FriendsView extends AbstractMainView {

    private final FriendFinder friendFinder;
    private final FriendsList friendsList;

    public FriendsView(TitleBar titleBar, FriendFinder friendFinder, FriendsList friendsList) {
        super(titleBar);
        this.friendFinder = friendFinder;
        this.friendsList = friendsList;
        init();
    }

    public void refresh() {
        removeAll();
        init();
    }

    private void init() {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setWidthFull();
        add(splitLayout);
        splitLayout.addToPrimary(friendsList);
        splitLayout.addToSecondary(friendFinder);
    }


    @Override
    public String getTitle() {
        return "Friends";
    }
}
