package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.atd.AtdRepository;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.util.AtdCache;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

@Push
public class MainView extends AppLayout {

    public MainView(AtdRepository atdRepository) {

        if (AtdCache.getAtds() == null) {
            AtdCache.setAtds(atdRepository.findAll());
        }


        setPrimarySection(AppLayout.Section.DRAWER);
        H3 h1 = new H3("The Hero Instant");
        addToNavbar(new DrawerToggle(), h1);

        Tabs tabs = new Tabs(
                new Tab( new RouterLink("Home", HomeView.class)),
                new Tab("Friends"),
                new Tab(new RouterLink("Collection (GMs)", CollectionView.class)),
                new Tab(new RouterLink("User Details", UserDetailsView.class)),
                new Tab( new Anchor("logout", "Logout "+ SecurityUtils.getLoggedInUser().getName())));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }
}
