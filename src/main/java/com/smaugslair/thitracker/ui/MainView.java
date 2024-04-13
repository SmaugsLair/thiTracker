package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.friends.FriendsView;
import com.smaugslair.thitracker.ui.games.CollectionView;
import com.smaugslair.thitracker.ui.games.GamesView;
import com.smaugslair.thitracker.ui.powers.PowerBrowserView;
import com.smaugslair.thitracker.ui.powers.PowerSetBrowserView;
import com.smaugslair.thitracker.ui.users.UserDetailsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

public class MainView extends AppLayout {

    private boolean newUI = true;
    public MainView(@Autowired SessionService sessionService) {

        User user = SecurityUtils.getLoggedInUser();

        H1 appTitle = new H1("The Hero Instant");
        appTitle.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("line-height", "var(--lumo-size-l)")
                .set("margin", "0 var(--lumo-space-m)");

        SideNav views = getPrimaryNavigation(user);

        Scroller scroller = new Scroller(views);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        DrawerToggle toggle = new DrawerToggle();
        toggle.setTooltipText("Click to toggle the nav bar");

        TitleBar viewTitle = new TitleBar();
        sessionService.setTitleBar(viewTitle);


        HorizontalLayout wrapper = new HorizontalLayout(toggle, viewTitle);
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setSpacing(false);


        addToDrawer(appTitle, scroller);
        addToNavbar(wrapper);

        setPrimarySection(Section.DRAWER);

    }

    private SideNav getPrimaryNavigation(User user) {
        SideNav sideNav = new SideNav();
        //SideNavItem heroesLink = new SideNavItem("Heroes", HeroView.class);
        sideNav.addItem(new SideNavItem("Heroes", HeroView.class));
        sideNav.addItem(new SideNavItem("Games", GamesView.class));
        sideNav.addItem(new SideNavItem("Friends", FriendsView.class));
        sideNav.addItem(new SideNavItem("Collection", CollectionView.class));
        sideNav.addItem(new SideNavItem("Power Sets", PowerSetBrowserView.class));
        sideNav.addItem(new SideNavItem("Powers", PowerBrowserView.class));
        if (user.isAdmin()) {
            sideNav.addItem(new SideNavItem("Users", UsersView.class));
            sideNav.addItem(new SideNavItem("Upload", PowersUploadView.class));
        }
        sideNav.addItem(new SideNavItem("User Details", UserDetailsView.class));
        sideNav.addItem(new SideNavItem("Logout", LogoutView.class));
        return sideNav;
    }

}
