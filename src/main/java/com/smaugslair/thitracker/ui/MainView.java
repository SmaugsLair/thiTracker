package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.friends.FriendsSession;
import com.smaugslair.thitracker.ui.games.CollectionView;
import com.smaugslair.thitracker.ui.games.GamesManager;
import com.smaugslair.thitracker.ui.powers.PowerBrowserView;
import com.smaugslair.thitracker.ui.powers.PowerSetBrowserView;
import com.smaugslair.thitracker.ui.users.UserDetailsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

@Push
public class MainView extends AppLayout {

    public MainView() {

        User user = SecurityUtils.getLoggedInUser();

        H3 h1 = new H3("The Hero Instant   ");

        Tabs tabs = new Tabs(
                new Tab( new RouterLink("Heros", HeroView.class)),
                new Tab( new RouterLink("Games", GamesManager.class)),
                new Tab(new RouterLink("Friends", FriendsSession.class)),
                new Tab(new RouterLink("Collection", CollectionView.class)),
                new Tab(new RouterLink("Power Sets", PowerSetBrowserView.class)),
                new Tab(new RouterLink("Powers", PowerBrowserView.class))
                );
        if (user.isAdmin()) {
            tabs.add(new Tab(new RouterLink("Users", UserAdmin.class)),
                new Tab(new RouterLink("Upload", PowersUpload.class)));
        }
        tabs.add(new Tab(new RouterLink("User Details", UserDetailsView.class)));
        tabs.add(new Tab( new Anchor("logout", "Logout "+ user.getName())));
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        addToNavbar(h1, tabs);
    }

}
