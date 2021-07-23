package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.friends.FriendsSession;
import com.smaugslair.thitracker.ui.games.CollectionView;
import com.smaugslair.thitracker.ui.powers.PowerBrowserView;
import com.smaugslair.thitracker.ui.powers.PowerSetBrowserView;
import com.smaugslair.thitracker.ui.users.UserDetailsView;
import com.smaugslair.thitracker.util.AtdCache;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Push
public class MainView extends AppLayout {

    private SessionService sessionService;

    public MainView() {

        setPrimarySection(AppLayout.Section.DRAWER);
        H3 h1 = new H3("The Hero Instant");
        addToNavbar(new DrawerToggle(), h1);

        Tabs tabs = new Tabs(
                new Tab( new RouterLink("Home", HomeView.class)),
                new Tab(new RouterLink("Friends", FriendsSession.class)),
                new Tab(new RouterLink("Collection (GMs)", CollectionView.class)),
                new Tab(new RouterLink("Power Sets", PowerSetBrowserView.class)),
                new Tab(new RouterLink("Power Browser", PowerBrowserView.class)),
                new Tab(new RouterLink("User Details", UserDetailsView.class)));
        if (SecurityUtils.getLoggedInUser().isAdmin()) {
            tabs.add(new Tab(new RouterLink("User Admin", UserAdmin.class)));
            tabs.add(new Tab(new RouterLink("Powers Upload", PowersUpload.class)));
        }
        tabs.add(new Tab( new Anchor("logout", "Logout "+ SecurityUtils.getLoggedInUser().getName())));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    @PostConstruct
    public void init() {
        if (AtdCache.getAtds() == null) {
            AtdCache.setAtds(sessionService.getAtdRepo().findAll());
        }
    }

    @Autowired
    public void setsessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

}
