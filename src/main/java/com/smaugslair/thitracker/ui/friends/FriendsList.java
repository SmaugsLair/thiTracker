package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class FriendsList extends VerticalLayout {

    private final SessionService sessionService;

    public FriendsList(SessionService sessionService) {
        this.sessionService = sessionService;
        init();
    }

    public void refresh() {
        removeAll();
        init();
    }

    private void init() {
        add(new H3("Friends list"));
        User user = SecurityUtils.getLoggedInUser();
        List<Friendship> friends = sessionService.getFriendsRepo().findAllByUserOrFriend(user, user);


        friends.forEach(friendship -> {
            User friend = friendship.getUser().equals(user) ? friendship.getFriend(): friendship.getUser();
            //NameValue nameValue = new NameValue("userId", friend.getId());
            List<PlayerCharacter> pcs = sessionService.getPcRepo().findAllByUserId(friend.getId());
            Details details = new Details(friend.getDisplayName(), new FriendsActions(friendship, pcs, this) );
            details.setOpened(!friendship.getAccepted());
            add(details);
        });


    }

    public void delete(Friendship friendship) {
        sessionService.getFriendsRepo().delete(friendship);
        refresh();
    }

    public void accept(Friendship friendship) {
        friendship.setAccepted(true);
        sessionService.getFriendsRepo().save(friendship);
        refresh();
    }
}
