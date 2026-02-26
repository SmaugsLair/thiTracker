package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.pc.HeroPowerRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.MessageRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.smaugslair.thitracker.ui.components.events.AppEventListener;
import com.smaugslair.thitracker.ui.friends.FriendsView;
import com.smaugslair.thitracker.ui.games.CollectionView;
import com.smaugslair.thitracker.ui.games.GameCreationView;
import com.smaugslair.thitracker.ui.players.HeroCreationView;
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
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UIScope
public class MainView extends AppLayout implements AfterNavigationObserver, AppEventListener {

    //private static final Logger log = LoggerFactory.getLogger(MainView.class);

    private final SessionService sessionService;

    //private final UIService uiService;

    //private final UserRepository userRepository;

    private final PlayerCharacterRepository playerCharacterRepository;

    private final GameRepository gameRepository;

    private final MessageRepository messageRepository;
    private final HeroPowerRepository heroPowerSetRepository;

    //private final TitleBar titleBar;

    //private boolean newUI = true;
    SideNavItem messagesItem;
    private final SideNav sideNav;
    //private ApplicationContext context;
    private SideNavItem heroesItem, gamesItem;

    public MainView(SessionService sessionService, UIService uiService, PlayerCharacterRepository playerCharacterRepository,
                    GameRepository gameRepository, MessageRepository messageRepository, HeroPowerRepository heroPowerSetRepository, TitleBar titleBar) {
        this.sessionService = sessionService;
        //this.uiService = uiService;
        //this.userRepository = userRepository;
        this.playerCharacterRepository = playerCharacterRepository;
        this.gameRepository = gameRepository;
        this.messageRepository = messageRepository;
        this.heroPowerSetRepository = heroPowerSetRepository;
        //this.titleBar = titleBar;

        uiService.addAppEventListener(this);

        User user = sessionService.getLoggedInUser();

        H1 appTitle = new H1("The Hero Instant");
        appTitle.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("line-height", "var(--lumo-size-l)")
                .set("margin", "0 var(--lumo-space-m)");
        appTitle.setClassName("appTitle");

        sideNav = new SideNav();
        getPrimaryNavigation(user).forEach(sideNavItem -> sideNav.addItem(sideNavItem));
        //sideNav.addItem(getPrimaryNavigation(user));
        //sideNav = getPrimaryNavigation(user);

        Scroller scroller = new Scroller(sideNav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        DrawerToggle toggle = new DrawerToggle();
        toggle.setTooltipText("Click to toggle the nav bar");

        HorizontalLayout wrapper = new HorizontalLayout(toggle, titleBar);
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setSpacing(false);


        addToDrawer(appTitle, scroller);
        addToNavbar(wrapper);

        setPrimarySection(Section.DRAWER);

    }

    private List<SideNavItem> getPrimaryNavigation(User user) {

        List<SideNavItem> items = new ArrayList<>();

        heroesItem = new SideNavItem("Heroes");
        heroesItem.setPrefixComponent(VaadinIcon.USER_STAR.create());
        items.add(heroesItem);

        Iterable<PlayerCharacter> pcs = playerCharacterRepository.findAllByUserId(user.getId())
                .stream().sorted().collect(Collectors.toList());

        for (PlayerCharacter pc : pcs) {
            heroesItem.addItem(generatePCSideNavItem(pc));
        }
        heroesItem.addItem(new SideNavItem("++", HeroCreationView.class));

        gamesItem = new SideNavItem("Games");
        gamesItem.setPrefixComponent(VaadinIcon.GAMEPAD.create());
        items.add(gamesItem);
        Iterable<Game> games = gameRepository.findAllByGameMasterId(user.getId()).stream().sorted().collect(Collectors.toList());
        for (Game g : games) {
            gamesItem.addItem(generateGameSideNavItem(g));
        }
        gamesItem.addItem(new SideNavItem("++", GameCreationView.class));


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

    private SideNavItem generateGameSideNavItem(Game game) {
        String name = game.getName();
        SideNavItem gameMenuItem = new SideNavItem(name);

        SideNavItem gameItem = new SideNavItem("Launch","/gmsession/"+game.getId());
        gameMenuItem.addItem(gameItem);

        SideNavItem gameDeletion = new SideNavItem("Delete", "/gamedeletion/"+game.getId());
        gameMenuItem.addItem(gameDeletion);

        return gameMenuItem;
    }

    private SideNavItem generatePCSideNavItem(PlayerCharacter pc) {
        String name = pc.getName();
        Game game = null;
        if (pc.getGameId() != null) {
            Optional<Game> oGame = gameRepository.findById(pc.getGameId());
            if (oGame.isPresent()) {
                game = oGame.get();
                name += " ("+game.getName()+")";
            }
        }
        SideNavItem heroItem = new SideNavItem(name,"/hero/"+pc.getId());
        if (game != null) {
            heroItem.addItem(new SideNavItem("Join "+game.getName(), "/playersession/"+pc.getId()));
        }

        if (!heroPowerSetRepository.findAllByPlayerCharacter(pc).isEmpty()) {
            SideNavItem printableSheet = new SideNavItem("Printable sheet", "/printableSheet/" + pc.getId());
            printableSheet.setOpenInNewBrowserTab(true);
            heroItem.addItem(printableSheet);

            SideNavItem printablePowers = new SideNavItem("Printable powers", "/printablePowers/" + pc.getId());
            printablePowers.setOpenInNewBrowserTab(true);
            heroItem.addItem(printablePowers);
        }

        SideNavItem heroNotes = new SideNavItem("Notes", "/pcnotes/"+pc.getId());
        heroItem.addItem(heroNotes);

        SideNavItem herodeletion = new SideNavItem("Delete "+pc.getName(), "/herodeletion/"+pc.getId());
        heroItem.addItem(herodeletion);

        return heroItem;
    }

    private void refreshMessagesItem() {

        User user = sessionService.getLoggedInUser();
        int count = messageRepository.findAllByUserId(user.getId()).size();
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
    public void onAppEvent(AppEvent appEvent) {
        if(AppEvent.AppEventType.MenuChange.equals(appEvent.getType())) {
            boolean heroesOpen = heroesItem.isExpanded();
            boolean gamesOpen = gamesItem.isExpanded();
            sideNav.removeAll();
            getPrimaryNavigation(sessionService.getUser()).forEach(sideNavItem -> sideNav.addItem(sideNavItem));
            heroesItem.setExpanded(heroesOpen);
            gamesItem.setExpanded(gamesOpen);
        }
    }

}
