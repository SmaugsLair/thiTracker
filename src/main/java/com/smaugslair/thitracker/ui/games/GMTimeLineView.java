package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.atd.ActionTime;
import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.game.ActionTimeDelta;
import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.CollectedDelta;
import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.games.tl.*;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class GMTimeLineView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(GMTimeLineView.class);

    private final SessionService sessionService;
    private final GMSession gmSession;

    private Game game = null;
    private Accordion gameAccordion;
    private boolean accordionOpen = false;



    public GMTimeLineView(GMSession gmSession, SessionService sessionService) {
        this.gmSession = gmSession;
        this.sessionService = sessionService;
        init();
    }


    public void refreshAndBroadcast() {
        refreshWithLog(null);
    }

    public void refreshWithLog(String logText) {
        accordionOpen = gameAccordion.getOpenedIndex().isPresent();
        removeAll();
        init();
        Entry entry = new Entry();
        entry.setText(logText);
        entry.setGameId(getGameId());
        entry.setType(EventType.GMAction);
        if (logText != null) {
            sessionService.getEntryRepo().save(entry);
            gmSession.logAction(entry);

        }
        Broadcaster.broadcast(entry);
    }

    private Long getGameId() {
        return sessionService.getGameId();
    }

    private Game confirmData() {

        if (getGameId() == null) {
            add(new H1("No Game loaded"));
            return null;
        }

        final Game game = sessionService.getGameRepo().findById(getGameId()).orElse(new Game());

        if (game.getId() == null) {
            add(new H1("Game not found!"));
            return null;
        }
        User user = SecurityUtils.getLoggedInUser();
        if (user == null || !game.getGameMasterId().equals(user.getId())) {
            add(new H1("You are not the GameMaster for this game!"));
            return null;
        }
        return game;

    }

    public void init() {
        game = confirmData();
        if (game == null) {
            return;
        }
        //gameIdProp = new NameValue("gameId", getGameId());

        gameAccordion = new Accordion();
        if (!accordionOpen) {
            gameAccordion.close();
        }
        HorizontalLayout gameActions = new HorizontalLayout();
        HorizontalLayout buttonBarTop = new HorizontalLayout();
        HorizontalLayout buttonBarBottom = new HorizontalLayout();
        VerticalLayout buttonZone = new VerticalLayout(buttonBarTop, buttonBarBottom);
        gameActions.setMargin(false);
        gameActions.setPadding(false);
        gameActions.add(createMaxDiceField());
        gameActions.add(buttonZone);
        buttonBarTop.add(createPCTimeLineButton());
        buttonBarTop.add(createAddEventButton());
        buttonBarTop.add(createImportButton());
        buttonBarTop.add(createInviteButton());
        buttonBarBottom.add(createResetStunButton());
        buttonBarBottom.add(createResetTimeButton());
        buttonBarBottom.add(createClearRollsButton());
        buttonBarBottom.add(createClearActionsButton());
        gameAccordion.add("Game: "+game.getName(), gameActions);
        add(gameAccordion);



        List<TimeLineItem> items = getTliRepo().findByGameId(getGameId()).stream()
                .sorted().collect(Collectors.toList());

        TimeLineItem lastEvent = null;
        if (game.getLastEventId() != null) {
            lastEvent = getTliRepo().findById(game.getLastEventId()).orElse(null);
        }

        List<ActionTimeDefault> atds = getAtds();

        for (TimeLineItem item : items) {
            if (lastEvent != null) {
                item.setReactTime(lastEvent.getTime() - item.getTime());
            }
            //Clearing here because of caching
            item.getActionTimes().clear();
            //item.setActionTime(null);
            item.getActionTimes().add(ActionSelect.unselectedActionTime);
            for (ActionTimeDefault atd: atds) {
                item.getActionTimes().add(
                        new ActionTime(atd.getName(),
                                atd.getTime() + (atd.isStunable() ? item.getStun() : 0)
                                        + item.getDeltas().get(atd.getName()).getDelta()));
            }
        }

        Grid<TimeLineItem> grid = new Grid<>();
        grid.setThemeName("min-padding");
        //grid.addThemeVariants(GridVariant.LUMO_COMPACT);

        grid.setHeightByRows(true);
        grid.setItems(items);

        grid.addComponentColumn(item -> new NameField(item, this)).setHeader("Character/NPC/Event");
        grid.addComponentColumn(item -> new StunField(item, this)).setHeader("Stun");
        grid.addComponentColumn(item -> new ActionSelect(item, this));
        grid.addComponentColumn(item -> new TimeField(item, this)).setHeader("Time");
        grid.addComponentColumn(ReactText::new).setHeader("React");
        Icon icon = VaadinIcon.EYE_SLASH.create();
        icon.setSize("16px");
        grid.addComponentColumn(item -> new HiddenField(item, this)).setHeader(icon);
        icon = VaadinIcon.CARET_UP.create();
        icon.setSize("16px");
        grid.addComponentColumn(item -> new DeltaButton(item, this)).setHeader(icon);

        grid.setClassNameGenerator(item -> item.getColor());
        grid.getColumns().forEach(itemColumn -> {
            itemColumn.setAutoWidth(true);
            itemColumn.setTextAlign(ColumnTextAlign.CENTER);
        });

        grid.addItemClickListener(event -> showHeroDetails(event.getItem()));


        ColorDialog colorDialog = new ColorDialog(this);
        GridContextMenu<TimeLineItem> contextMenu = new GridContextMenu<>(grid);
        GridMenuItem<TimeLineItem> colorMenu = contextMenu.addItem("Color", event -> {
            if (event.getItem().isPresent()) {
                colorDialog.openWith(event.getItem().get());
            }
        });

        contextMenu.addItem("Copy", event -> event.getItem().ifPresent(item -> {
            TimeLineItem copy = new TimeLineItem();
            copy.setTime(item.getTime());
            copy.setName(item.getName());
            copy.setStun(item.getStun());
            copy.setHidden(item.getHidden());
            copy.setGameId(item.getGameId());
            copy.setColor(item.getColor());
            for (ActionTimeDelta delta: item.getDeltas().values()) {
                ActionTimeDelta deltaCopy = new ActionTimeDelta();
                deltaCopy.setDelta(delta.getDelta());
                deltaCopy.setName(delta.getName());
                copy.getDeltas().put(delta.getName(), deltaCopy);
            }
            getTliRepo().save(copy);
            refreshAndBroadcast();
        }));

        contextMenu.addItem("Remove", event -> event.getItem().ifPresent(item -> {
            if(item.getId().equals(game.getLastEventId())) {
                game.setLastEventId(null);
                sessionService.getGameRepo().save(game);
            }
            if (item.getPcId() != null) {
                sessionService.getPcRepo().findById(item.getPcId()).ifPresent(pc -> {
                    pc.setGameId(null);
                    sessionService.getPcRepo().save(pc);
                });
            }
            getTliRepo().delete(item);
            refreshAndBroadcast();
        }));

        contextMenu.addItem("Collect", event -> event.getItem().ifPresent(item -> {
            CollectedItem collectedItem = new CollectedItem();
            collectedItem.setColor(item.getColor());
            collectedItem.setName(item.getName());
            collectedItem.setGmId(game.getGameMasterId());
            for (ActionTimeDelta delta : item.getDeltas().values()) {
                CollectedDelta collectedDelta = new CollectedDelta();
                collectedDelta.setDelta(delta.getDelta());
                collectedDelta.setName(delta.getName());
                collectedItem.getDeltas().add(collectedDelta);
            }
            sessionService.getCiRepo().save(collectedItem);
            Component oldButton = buttonBarTop.getComponentAt(1);
            buttonBarTop.replace(oldButton, createImportButton());
            Notification.show("Saved "+collectedItem.getName()+" to your collection",
                    2000, Notification.Position.TOP_CENTER);
        }));

        add(grid);



    }

    private void sendPlayerInvitation(String name, String email) {

        User gm = SecurityUtils.getLoggedInUser();
        if (gm == null) {
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setCc(gm.getEmail());

        msg.setSubject("Invitation to The Hero Instant App");

        String sb = "Hello " + name + "\n You've been invited to create an account on The Hero Instance app and join a game with " +
                gm.getDisplayName() + "!\n" + "To start, point your browser to " +
                sessionService.getThiProperties().getAppUrl() + ", create an account, sign in, click on the Friends tab, " +
                "and enter \nUsername: " + gm.getName() + "\nFriend code: " +
                gm.getFriendCode() + "\nin the Friend Finder. Then create a character and " +
                gm.getDisplayName() + " will be able to add you to their game.";
        msg.setText(sb);

        sessionService.getJavaMailSender().send(msg);
    }

    public void updateItem(TimeLineItem item) {

        String logText = null;
        if (item.getActionTime() != null && !ActionSelect.unselectedActionTime.equals(item.getActionTime())) {
            item.setTime(item.getTime() + item.getActionTime().time);
            Game game = sessionService.getGameRepo().findById(item.getGameId()).orElse(null);
            if (game != null) {
                game.setLastEventId(item.getId());
                sessionService.getGameRepo().save(game);
            }
            StringBuilder sb = new StringBuilder(item.getName()).append(" spent ").append(item.getActionTime().time);
            sb.append(" TU on a ").append(item.getActionTime().name).append(" action");
            logText = sb.toString();
        }
        getTliRepo().save(item);
        refreshWithLog(logText);
    }

    public Long getGameID() {
        return sessionService.getGameId();
    }


    public void showHeroDetails(TimeLineItem item) {
        if (item.getPcId() != null) {
            sessionService.getPcRepo().findById(item.getPcId()).ifPresent(playerCharacter -> {
                gmSession.setHero(playerCharacter);
            });
        }
        else {
            gmSession.setHero(null);
        }
    }

    public PlayerCharacter updatePc(PlayerCharacter pc) {
        //log.info("updatePc");
        pc = sessionService.getPcRepo().save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(getGameId());
        Broadcaster.broadcast(entry);
        removeAll();
        init();
        return pc;
    }

    public List<ActionTimeDefault> getAtds() {
        return sessionService.getAtdRepo().findAll();
    }

    private void clearLogs(EventType type) {
        //NameValue nameValue = new NameValue("gameId", getGameId());
        List<Entry> list = sessionService.getEntryRepo().findByGameId(getGameId());
        for (Entry entry : list) {
            if (entry.getType().equals(type)) {
                sessionService.getEntryRepo().delete(entry);
            }
        }
    }
    private Button createClearRollsButton() {
        return new UserSafeButton("Clear rolls", event -> {
            clearLogs(EventType.DiceRoll);
            gmSession.clearRolls();
            Entry entry = new Entry();
            entry.setGameId(getGameId());
            entry.setType(EventType.ClearRolls);
            Broadcaster.broadcast(entry);
        });
    }

    private Button createClearActionsButton() {
        return new UserSafeButton("Clear actions", event -> {
            clearLogs(EventType.GMAction);
            gmSession.clearActions();
        });
    }
    private Button createPCTimeLineButton() {
        return new UserSafeButton("PC Timeline", event -> {
            getUI().ifPresent(ui -> {
                ui.getPage().open("pcTimeLineView", "PC Timeline");
            });
        });
    }

    private Button createResetTimeButton() {
        return new UserSafeButton("Reset time", event -> {
            Iterable<TimeLineItem> timeLineItems = getTliRepo().findByGameId(getGameId());
            timeLineItems.forEach(item -> {
                item.setTime(0);
                getTliRepo().save(item);
            });
            game.setLastEventId(null);
            sessionService.getGameRepo().save(game);
            refreshAndBroadcast();
        });
    }

    private Button createResetStunButton() {

        Button resetStun = new UserSafeButton("Reset stun");
        resetStun.addClickListener(event -> {
            Iterable<TimeLineItem> timeLineItems = getTliRepo().findByGameId(getGameId());
            timeLineItems.forEach(item -> {
                item.setStun(0);
                getTliRepo().save(item);
            });
            refreshAndBroadcast();
        });
        return resetStun;
    }

    private Button createAddEventButton() {
        User user = SecurityUtils.getLoggedInUser();
        if (user == null) {
            return new UserSafeButton();
        }

        List<Friendship> friendships = sessionService.getFriendsRepo().findAllByUserOrFriend(user, user);

        Set<Integer> userIds = new HashSet<>();
        userIds.add(user.getId());
        friendships.forEach(friendship -> {
            userIds.add(friendship.getUser().getId());
            userIds.add(friendship.getFriend().getId());
        });

        List<PlayerCharacter> pcs = new ArrayList<>();
        userIds.forEach(id -> {
            pcs.addAll(sessionService.getPcRepo().findAllByUserId(id).stream()
                    .filter(pc -> pc.getGameId() == null ).collect(Collectors.toList()));
        });

        NewItemForm newItemForm = new NewItemForm(pcs, sessionService);
        ConfirmDialog addItemDialog = new ConfirmDialog(newItemForm);

        Button confirmButton = new UserSafeButton("Add event to "+game.getName(), event -> {
            TimeLineItem item = new TimeLineItem();
            if (newItemForm.isPC()) {
                PlayerCharacter pc = newItemForm.getPC();
                User player = sessionService.getUserRepository().findById(pc.getUserId()).orElse(new User());
                item.setName(pc.getCharacterAndPlayerName(user));
                item.setPcId(pc.getId());
                pc.setGameId(getGameId());
                sessionService.getPcRepo().save(pc);
            }
            else {
                item.setName(newItemForm.getName());
            }
            item.setStun(newItemForm.getStun());
            item.setTime(newItemForm.getTime());
            item.setHidden(newItemForm.getHidden());
            item.setGameId(game.getId());
            item.initializeDeltas(getAtds());
            getTliRepo().save(item);
            addItemDialog.close();
            refreshAndBroadcast();
        });
        addItemDialog.setConfirmButton(confirmButton);
        addItemDialog.setWidth("500px");

        Button addButton = new UserSafeButton("Add event");
        addButton.addClickListener(event -> addItemDialog.open());
        return addButton;
    }

    private Button createImportButton() {
        Button importButton = new UserSafeButton("Import");
        List<CollectedItem> collectedItems =
            sessionService.getCiRepo().findAllByGmId(SecurityUtils.getLoggedInUser().getId());
        if (!collectedItems.isEmpty()) {
            Grid<CollectedItem> ciGrid = new Grid<>();
            ciGrid.setThemeName("min-padding");
            ciGrid.setItems(collectedItems);
            ciGrid.setHeightByRows(true);
            ciGrid.setClassNameGenerator(item -> item.getColor());
            ciGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
            ciGrid.addColumn(CollectedItem::getName);
            ciGrid.addComponentColumn((CollectedItem item1) -> new ImportButton(item1, this));

            ConfirmDialog importDialog = new ConfirmDialog(ciGrid);
            importDialog.setWidth("500px");

            importButton.addClickListener( event -> importDialog.open());
        }
        else {
            importButton.setEnabled(false);
        }
        return importButton;
    }

    private Button createInviteButton() {

        TextField inviteName = new TextField();
        inviteName.setPlaceholder("name");
        TextField inviteEmail = new TextField();
        inviteEmail.setPlaceholder("email");

        ConfirmDialog inviteDialog = new ConfirmDialog(new VerticalLayout(inviteName, inviteEmail));
        Button inviteButton = new UserSafeButton("Invite player", event -> {
            sendPlayerInvitation(inviteName.getValue(), inviteEmail.getValue());
            inviteDialog.close();
        });
        inviteDialog.setConfirmButton(inviteButton);
        return new UserSafeButton("Invite", event -> inviteDialog.open());
    }

    private IntegerField createMaxDiceField() {

        IntegerField maxDice = new IntegerField();
        maxDice.setLabel("Max dice");
        maxDice.setValue(game.getMaxDice());
        maxDice.setHasControls(true);
        maxDice.setMin(1);
        maxDice.addValueChangeListener(event -> {
            game.setMaxDice(event.getValue());
            sessionService.getGameRepo().save(game);
            Entry entry = new Entry(EventType.MaxDiceUpdate);
            entry.setGameId(getGameId());
            Broadcaster.broadcast(entry);

        });
        return maxDice;
    }

    public TimeLineItemRepository getTliRepo() {
        return sessionService.getTliRepo();
    }

    public EntryRepository getEntryRepo() {
        return sessionService.getEntryRepo();
    }
}
