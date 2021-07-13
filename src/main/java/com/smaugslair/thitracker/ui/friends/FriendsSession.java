package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.user.FriendshipRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.util.RepoService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addons.searchbox.SearchBox;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Friends Page")
@Route(value = "friends", layout = MainView.class)
public class FriendsSession extends SplitLayout {
    private final RepoService repoService;

    //private final UserRepository userRepo;
    //List<User> users;

    public FriendsSession(RepoService repoService) {
        this.repoService = repoService;
        init();
    }

    public void refresh() {
        removeAll();
        init();
    }

    private void init() {
        addToPrimary(new FriendsList(repoService));
        addToSecondary(new FriendFinder(this, repoService));
    }




}
