package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.FriendshipRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class FriendsList extends VerticalLayout {


    private final SessionService sessionService;

    private final FriendshipRepository friendshipRepository;

    private final PlayerCharacterRepository playerCharacterRepository;

    public FriendsList(SessionService sessionService, FriendshipRepository friendshipRepository, PlayerCharacterRepository playerCharacterRepository) {
        this.sessionService = sessionService;
        this.friendshipRepository = friendshipRepository;
        this.playerCharacterRepository = playerCharacterRepository;
    }

    public void refresh() {
        removeAll();
        init();
    }

    @PostConstruct
    public void init() {
        User user = sessionService.getLoggedInUser();
        List<Friendship> friends = friendshipRepository.findAllByUserOrFriend(user, user);


        friends.forEach(friendship -> {
            User friend = friendship.getUser().equals(user) ? friendship.getFriend(): friendship.getUser();
            //NameValue nameValue = new NameValue("userId", friend.getId());
            List<PlayerCharacter> pcs = playerCharacterRepository.findAllByUserId(friend.getId());
            Details details = new Details(friend.getDisplayName(), new FriendsActions(user, friendship, pcs, this) );
            details.setOpened(!friendship.getAccepted());
            add(details);
        });


    }

    public void delete(Friendship friendship) {
        friendshipRepository.delete(friendship);
        refresh();
    }

    public void accept(Friendship friendship) {
        friendship.setAccepted(true);
        friendshipRepository.save(friendship);
        refresh();
    }
}
