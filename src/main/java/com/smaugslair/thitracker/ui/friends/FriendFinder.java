package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.util.RepoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Optional;


public class FriendFinder extends VerticalLayout {

    private final RepoService repoService;
    private final FriendsSession friendsSession;
    private User self = SecurityUtils.getLoggedInUser();
    private User friend = null;

    public FriendFinder(FriendsSession friendsSession, RepoService repoService) {
        this.repoService = repoService;
        this.friendsSession = friendsSession;
        init();
    }

    private void init() {
        add("Friend Finder");
        TextField nameField = new TextField();
        nameField.setPlaceholder("Username");
        add(nameField);

        TextField friendCode = new TextField();
        nameField.setPlaceholder("Friend code");
        add(friendCode);

        Label message = new Label();

        Button request = new Button("", event -> {
            if (friend == null) {
                return;
            }
            Friendship friendship = new Friendship();
            friendship.setAccepted(false);
            friendship.setUser(self);
            friendship.setFriend(friend);
            repoService.getFriendsRepo().save(friendship);
            friendsSession.refresh();
        });
        request.setVisible(false);

        Button button = new Button("Find", event -> {
            request.setVisible(false);
            Optional<User> user = repoService.getUserRepo().findUserByNameAndFriendCode(nameField.getValue(), friendCode.getValue());
            if (user.isPresent()) {
                if (user.get().equals(self)) {
                    message.setText("That's you, fool!");
                    return;
                }
                Optional<Friendship> friendship = repoService.getFriendsRepo().findByUserAndFriend(user.get(), self);
                if (friendship.isPresent()) {
                    if (friendship.get().getAccepted()) {
                        message.setText("Already friends with " + user.get().getDisplayName());
                    }
                    else {
                        message.setText(user.get().getDisplayName()+ " has sent a request to you, check your friends lists to accept");
                    }
                    return;
                }
                friendship = repoService.getFriendsRepo().findByUserAndFriend(self, user.get());
                if (friendship.isPresent()) {
                    if (friendship.get().getAccepted()) {
                        message.setText("Already friends with " + user.get().getDisplayName());
                    }
                    else {
                        message.setText("Still waiting for " + user.get().getDisplayName()+" to accept");
                    }
                    return;
                }
                friend = user.get();
                message.setText("Found! " +friend.getDisplayName());
                request.setText("Send friend request to " + friend.getDisplayName());
                request.setVisible(true);
            }
            else {
                message.setText("Not found");
            }
        });
        add(button);
        add(message);
        add(request);

    }
}
