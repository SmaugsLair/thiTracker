package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.events.HeroCountEvent;
import com.smaugslair.thitracker.ui.components.events.HeroCountListener;
import com.smaugslair.thitracker.ui.friends.FriendsView;
import com.smaugslair.thitracker.ui.games.CollectionView;
import com.smaugslair.thitracker.ui.games.GamesView;
import com.smaugslair.thitracker.ui.powers.PowerBrowserView;
import com.smaugslair.thitracker.ui.powers.PowerSetBrowserView;
import com.smaugslair.thitracker.ui.templates.TemplateView;
import com.smaugslair.thitracker.ui.users.UserDetailsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainView extends AppLayout implements AfterNavigationObserver, HeroCountListener {

    private static final Logger log = LoggerFactory.getLogger(MainView.class);
    private final SessionService sessionService;
    private boolean newUI = true;
    SideNavItem messagesItem;
    private SideNav sideNav;

    public MainView(@Autowired SessionService sessionService) {

        //log.info("Constructor");
        this.sessionService = sessionService;

        sessionService.addHeroCountListener(this);

        User user = SecurityUtils.getLoggedInUser(sessionService);

        H1 appTitle = new H1("The Hero Instant");
        appTitle.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("line-height", "var(--lumo-size-l)")
                .set("margin", "0 var(--lumo-space-m)");
        appTitle.setClassName("appTitle");

        sideNav = new SideNav();
        getPrimaryNavigation(user).stream().forEach(sideNavItem -> {sideNav.addItem(sideNavItem);});
        //sideNav.addItem(getPrimaryNavigation(user));
        //sideNav = getPrimaryNavigation(user);

        Scroller scroller = new Scroller(sideNav);
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

    private List<SideNavItem> getPrimaryNavigation(User user) {

        List<SideNavItem> items = new ArrayList<>();

        SideNavItem heroesItem = new SideNavItem("Heroes");
        heroesItem.setPrefixComponent(VaadinIcon.USER_STAR.create());
        items.add(heroesItem);

        Iterable<PlayerCharacter> pcs = sessionService.getPcRepo().findAllByUserId(user.getId())
                .stream().sorted().collect(Collectors.toList());

        for (PlayerCharacter pc : pcs) {
            String name = pc.getName();
            Game game = null;
            if (pc.getGameId() != null) {
                game = sessionService.getGameRepo().findById(pc.getGameId()).orElse(null);
                name += " ("+game.getName()+")";
            }
            heroesItem.addItem(new SideNavItem(name, "/hero/"+pc.getId()));
        }
        heroesItem.addItem(new SideNavItem("++", "/hero/new"));

        SideNavItem gamesItem = new SideNavItem("Games", GamesView.class);
        gamesItem.setPrefixComponent(VaadinIcon.GAMEPAD.create());

        items.add(gamesItem);


        SideNavItem userItem = new SideNavItem(user.getDisplayName());
        userItem.setPrefixComponent(VaadinIcon.USER.create());
        userItem.addItem(new SideNavItem("Details", UserDetailsView.class));
        userItem.addItem(new SideNavItem("Friends", FriendsView.class));
        userItem.addItem(new SideNavItem("Collection", CollectionView.class));
        items.add(userItem);

        SideNavItem referenceItem = new SideNavItem("References");
        referenceItem.setPrefixComponent(VaadinIcon.BOOK.create());
        referenceItem.addItem(new SideNavItem("Power Sets", PowerSetBrowserView.class));
        referenceItem.addItem(new SideNavItem("Powers", PowerBrowserView.class));
        items.add(referenceItem);

        if (user.isAdmin()) {
            SideNavItem adminItem = new SideNavItem("Admin");
            adminItem.setPrefixComponent(VaadinIcon.COG.create());
            adminItem.addItem(new SideNavItem("Users", UsersView.class));
            adminItem.addItem(new SideNavItem("Upload", PowersUploadView.class));
            adminItem.addItem(new SideNavItem("Templates", TemplateView.class));
            items.add(adminItem);
        }

        items.add(new SideNavItem(""));

        items.add(new SideNavItem("Logout", LogoutView.class));

        return items;
    }

    private void refreshMessagesItem() {

        User user = SecurityUtils.getLoggedInUser(sessionService);
        int count = sessionService.getMessageRepository().findAllByUserId(user.getId()).size();
        if (count > 0) {
            messagesItem = new SideNavItem("Messages", MessageView.class);
            messagesItem.setSuffixComponent(new Span(String.valueOf(count)));
        }
        else {
            messagesItem = null;
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (messagesItem != null) {
            sideNav.remove(messagesItem);
        }
        refreshMessagesItem();
        if (messagesItem != null) {
            sideNav.addItemAsFirst(messagesItem);
        }

    }

    @Override
    public void heroCountChanged(HeroCountEvent event) {

        log.info("heroCountChanged");

        //sideNav = getPrimaryNavigation(sessionService.getUser());
        sideNav.removeAll();

        getPrimaryNavigation(sessionService.getUser()).stream().forEach(sideNavItem -> {sideNav.addItem(sideNavItem);});


    }
}
