package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.mail.SimpleMailMessage;

import java.util.Optional;


public class FriendFinder extends VerticalLayout {

    private final SessionService sessionService;
    private final FriendsSession friendsSession;
    private final User self = SecurityUtils.getLoggedInUser();
    private User friend = null;

    public FriendFinder(FriendsSession friendsSession, SessionService sessionService) {
        this.sessionService = sessionService;
        this.friendsSession = friendsSession;
        init();
    }

    private void init() {
        add("Friend Finder");
        TextField nameField = new TextField();
        nameField.setPlaceholder("Username");
        add(nameField);

        TextField friendCode = new TextField();
        friendCode.setPlaceholder("Friend code");
        add(friendCode);

        Label message = new Label();

        Button request = new UserSafeButton("", event -> {
            if (friend == null) {
                return;
            }
            Friendship friendship = new Friendship();
            friendship.setAccepted(false);
            friendship.setUser(self);
            friendship.setFriend(friend);
            sessionService.getFriendsRepo().save(friendship);
            //sendEmail(friend);
            friendsSession.refresh();
        });
        request.setVisible(false);

        Button button = new UserSafeButton("Find", event -> {
            request.setVisible(false);
            User user = sessionService.getUserRepository().findUserByName(nameField.getValue());
            if (user != null) {
                if (user.equals(self)) {
                    message.setText("That's you, fool!");
                    return;
                }
                if (!user.getFriendCode().equals(friendCode.getValue())) {
                    message.setText("Not found");
                }
                Optional<Friendship> friendship = sessionService.getFriendsRepo().findByUserAndFriend(user, self);
                if (friendship.isPresent()) {
                    if (friendship.get().getAccepted()) {
                        message.setText("Already friends with " + user.getDisplayName());
                    }
                    else {
                        message.setText(user.getDisplayName()+ " has sent a request to you, check your friends lists to accept");
                    }
                    return;
                }
                friendship = sessionService.getFriendsRepo().findByUserAndFriend(self, user);
                if (friendship.isPresent()) {
                    if (friendship.get().getAccepted()) {
                        message.setText("Already friends with " + user.getDisplayName());
                    }
                    else {
                        message.setText("Still waiting for " + user.getDisplayName()+" to accept");
                    }
                    return;
                }
                friend = user;
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

    private void sendEmail(User friend) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("naganalf@gmail.com");
        msg.setFrom("naganalf@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        sessionService.getJavaMailSender().send(msg);
    }
}
