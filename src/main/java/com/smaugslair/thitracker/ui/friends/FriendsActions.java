package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class FriendsActions extends HorizontalLayout {

    private final Friendship friendship;

    public FriendsActions(Friendship friendship, FriendsList parent) {
        this.friendship = friendship;
        if (friendship.getAccepted()) {
            Button deleteButton = new Button("Remove");
            add(new Button("Remove", event -> parent.delete(friendship)));
        }
        else {
            if (SecurityUtils.getLoggedInUser().equals(friendship.getUser())) {
                add(new Label("Waiting..."));
                add(new Button("Cancel", event -> parent.delete(friendship)));
            }
            else {
                add(new Button("Confirm", event -> parent.accept(friendship)));
                add(new Button("Reject", event -> parent.delete(friendship)));
            }

        }
    }
}
