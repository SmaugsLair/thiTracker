package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.FriendshipRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.util.RepoService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendsList extends VerticalLayout {

    private final RepoService repoService;

    public FriendsList(RepoService repoService) {
        this.repoService = repoService;
        init();
    }

    public void refresh() {
        removeAll();
        init();
    }

    private void init() {
        add(new H3("Friends list"));
        User user = SecurityUtils.getLoggedInUser();
        List<Friendship> friends = repoService.getFriendsRepo().findAllByUserOrFriend(user, user);

        Accordion accordion = new Accordion();
        add(accordion);

        friends.forEach(friendship -> {
            User name = friendship.getUser().equals(user) ? friendship.getFriend(): friendship.getUser();
            accordion.add(name.getDisplayName(), new FriendsActions(friendship, this) );
        });
        accordion.close();


    }

    public void delete(Friendship friendship) {
        repoService.getFriendsRepo().delete(friendship);
        refresh();
    }

    public void accept(Friendship friendship) {
        friendship.setAccepted(true);
        repoService.getFriendsRepo().save(friendship);
        refresh();
    }
}
