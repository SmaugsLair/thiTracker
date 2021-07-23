package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.atd.ActionTime;
import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.game.*;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.CollectedDelta;
import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.ci.ImportButton;
import com.smaugslair.thitracker.ui.games.tl.*;
import com.smaugslair.thitracker.util.AtdCache;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.Broadcaster;
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
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import java.util.*;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
public class GMTimeLineView extends VerticalLayout {

    private static Logger log = LoggerFactory.getLogger(GMTimeLineView.class);

    private final SessionService sessionService;
    private final GMSession gmSession;

    private TokenDialog tokenDialog = new TokenDialog(this);


    public GMTimeLineView(GMSession gmSession, SessionService sessionService ) {
        this.gmSession = gmSession;
        this.sessionService = sessionService;
        init();
    }


    public void refreshAndBroadcast() {
        refreshWithLog(null);
    }

    public void refreshWithLog(String logText) {
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
            add(new H1("Select a game before accessing a timeline"));
            return null;
        }

        final Game game = getGameRepo().findById(getGameId()).orElse(new Game());

        if (game.getId() == null) {
            add(new H1("Game not found!"));
            return null;
        }
        if (!game.getGameMasterId().equals(SecurityUtils.getLoggedInUser().getId())) {
            add(new H1("You are not the GameMaster for this game!"));
            return null;
        }
        return game;

    }

    public void init() {

        Game game = confirmData();
        if (game == null) {
            return;
        }
        Accordion gameAccordion = new Accordion();
        HorizontalLayout gameActions = new HorizontalLayout();
        gameAccordion.add("Game: "+game.getName(), gameActions);
        gameAccordion.close();

        add(gameAccordion);

        List<TimeLineItem> items = getTliRepo().findByGameIdOrderByTime(game.getId());

        TimeLineItem lastEvent = null;
        if (game.getLastEventId() != null) {
            lastEvent = getTliRepo().findById(game.getLastEventId()).orElse(null);
        }

        for (TimeLineItem item : items) {
            if (lastEvent != null) {
                item.setReactTime(lastEvent.getTime() - item.getTime());
            }
            item.getActionTimes().add(ActionSelect.unselectedActionTime);
            for (ActionTimeDefault atd: AtdCache.getAtds()) {
                item.getActionTimes().add(
                        new ActionTime(atd.getName(),
                                atd.getTime() + (atd.isStunable() ? item.getStun() : 0)
                                        + item.getDeltas().get(atd.getName()).getDelta()));
            }
        }

        Grid<TimeLineItem> grid = new Grid<>();
        grid.setHeightByRows(true);
        grid.setItems(items);

        //Icon icon = VaadinIcon.WALLET.create();

        grid.addComponentColumn(item -> new NameField(item, this)).setHeader("Character/NPC/Event");
        grid.addComponentColumn(item -> new TokenButton(item, this));
        grid.addComponentColumn(item -> new StunField(item, this)).setHeader("Stun");
        grid.addComponentColumn(item -> new ActionSelect(item, this));
        grid.addComponentColumn(item -> new TimeField(item, this)).setHeader("Time");
        grid.addComponentColumn(ReactText::new).setHeader("React");
        Icon icon = VaadinIcon.EYE_SLASH.create();
        icon.setSize("16px");
        grid.addComponentColumn(item -> new HiddenField(item, this)).setHeader(icon).setTextAlign(ColumnTextAlign.CENTER);
        icon = VaadinIcon.CARET_UP.create();
        icon.setSize("16px");
        grid.addComponentColumn(item -> new DeltaButton(item, this)).setHeader(icon).setTextAlign(ColumnTextAlign.CENTER);
        //grid.addComponentColumn((TimeLineItem item1) -> new ButtonRow(item1, this));

        grid.setClassNameGenerator(item -> "w3-"+item.getColor());
        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));


        ColorDialog colorDialog = new ColorDialog(this);
        GridContextMenu<TimeLineItem> contextMenu = new GridContextMenu<>(grid);
        GridMenuItem<TimeLineItem> colorMenu = contextMenu.addItem("Color", event -> {
            colorDialog.openWith(event.getItem());
        });

        contextMenu.addItem("Copy", event -> {
            event.getItem().ifPresent(item -> {
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
            });
        });

        contextMenu.addItem("Remove", event -> {
            event.getItem().ifPresent(item -> {
                getTliRepo().delete(item);
                refreshAndBroadcast();
            });
        });

        contextMenu.addItem("Collect", event -> {
            event.getItem().ifPresent(item -> {
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
                Notification.show("Saved "+collectedItem.getName()+" to your collection",
                        2000, Notification.Position.TOP_CENTER);
            });
        });

        add(grid);

        User user = SecurityUtils.getLoggedInUser();

        List<Friendship> friendships = sessionService.getFriendsRepo().findAllByUserOrFriend(user, user);

        Set<Integer> userIds = new HashSet<>();
        friendships.forEach(friendship -> {
            userIds.add(friendship.getUser().getId());
            userIds.add(friendship.getFriend().getId());
        });

        List<PlayerCharacter> pcs = new ArrayList<>();
        userIds.forEach(id -> {
            pcs.addAll(sessionService.getPcRepo().findAllByUserIdAndGameIdIsNull(id));
        });

        NewItemForm newItemForm = new NewItemForm(pcs);
        ConfirmDialog addItemDialog = new ConfirmDialog(newItemForm);

        Button confirmButton = new Button("Add event to "+game.getName(), event -> {
            TimeLineItem item = new TimeLineItem();
            if (newItemForm.isPC()) {
                PlayerCharacter pc = newItemForm.getPC();
                item.setName(pc.getCharacterAndPlayerName());
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
            item.initializeDeltas();
            getTliRepo().save(item);
            addItemDialog.close();
            refreshAndBroadcast();
        });
        addItemDialog.setConfirmButton(confirmButton);
        addItemDialog.setWidth("500px");


        Button addButton = new Button("Add event");
        addButton.addClickListener(event -> addItemDialog.open());
        gameActions.add(addButton);

        Button resetStun = new Button("Reset stun");
        resetStun.addClickListener(event -> {
            Iterable<TimeLineItem> timeLineItems = getTliRepo().findByGameIdOrderByTime(getGameId());
            timeLineItems.forEach(item -> item.setStun(0));
            getTliRepo().saveAll(timeLineItems);
            refreshAndBroadcast();
        });
        gameActions.add(resetStun);


        Button resetTime = new Button("Reset time", event -> {
            Iterable<TimeLineItem> timeLineItems = getTliRepo().findByGameIdOrderByTime(getGameId());
            timeLineItems.forEach(item -> item.setTime(0));
            getTliRepo().saveAll(timeLineItems);
            game.setLastEventId(null);
            getGameRepo().save(game);
            refreshAndBroadcast();
        });
        gameActions.add(resetTime);

        Button clearRollHistory = new Button("Clear roll log", event -> {
            getEntryRepo().deleteAllByGameIdAndType(getGameId(), EventType.DiceRoll);
            gmSession.clearRolls();
            Entry entry = new Entry();
            entry.setGameId(getGameId());
            entry.setType(EventType.ClearRolls);
            Broadcaster.broadcast(entry);
        });
        gameActions.add(clearRollHistory);

        Button clearActionHistory = new Button("Clear action log", event -> {
            getEntryRepo().deleteAllByGameIdAndType(getGameId(), EventType.GMAction);
            gmSession.clearActions();
        });
        gameActions.add(clearActionHistory);



        List<CollectedItem> collectedItems = sessionService.getCiRepo().findAllByGmId(SecurityUtils.getLoggedInUser().getId());
        if (!collectedItems.isEmpty()) {

            Grid<CollectedItem> ciGrid = new Grid<>();
            ciGrid.setItems(collectedItems);
            ciGrid.setHeightByRows(true);
            ciGrid.setClassNameGenerator(item -> "w3-"+item.getColor());
            ciGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
            ciGrid.addColumn(CollectedItem::getName);
            ciGrid.addComponentColumn((CollectedItem item1) -> new ImportButton(item1, this));

            ConfirmDialog importDialog = new ConfirmDialog(ciGrid);
            importDialog.setWidth("500px");

            Button importButton = new Button("Import from collection", event -> {
                importDialog.open();
            });
            gameActions.add(importButton);

        }

        TextField inviteName = new TextField();
        inviteName.setPlaceholder("name");
        TextField inviteEmail = new TextField();
        inviteEmail.setPlaceholder("email");

        ConfirmDialog inviteDialog = new ConfirmDialog(new VerticalLayout(inviteName, inviteEmail));
        Button inviteButton = new Button("Invite player", event -> {
            sendPlayerInvitation(inviteName.getValue(), inviteEmail.getValue());
            inviteDialog.close();
        });
        inviteDialog.setConfirmButton(inviteButton);
        gameActions.add(new Button("Invite players", event -> {
            inviteDialog.open();
        }));

    }

    private void sendPlayerInvitation(String name, String email) {

        User gm = SecurityUtils.getLoggedInUser();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Invitation to The Hero Instant App");

        StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append(name).append("\n You've been invited to create an account on The Hero Instance app and join a game with ")
            .append(gm.getDisplayName()).append("!\n").append("To start, point your browser to ")
                .append(sessionService.getThiProperties().getAppUrl()).append(", create an account, sign in, click on the Friends tab, ")
                .append("and enter \nUsername: ").append(gm.getName()).append("\nFriend code: ")
                .append(gm.getFriendCode()).append("\nin the Friend Finder. Then create a character and ")
                .append(gm.getDisplayName()).append(" will be able to add you to their game.");
        msg.setText(sb.toString());

        sessionService.getJavaMailSender().send(msg);
    }

    public void updateItem(TimeLineItem item) {

        String logText = null;
        if (item.getActionTime() != null && !ActionSelect.unselectedActionTime.equals(item.getActionTime())) {
            item.setTime(item.getTime() + item.getActionTime().time);
            Game game = getGameRepo().findById(item.getGameId()).orElse(null);
            if (game != null) {
                game.setLastEventId(item.getId());
                getGameRepo().save(game);
            }
            StringBuffer sb = new StringBuffer(item.getName()).append(" spent ").append(item.getActionTime().time);
            sb.append(" TU on a ").append(item.getActionTime().name).append(" action");
            logText = sb.toString();
        }
        getTliRepo().save(item);
        refreshWithLog(logText);
    }

    public GameRepository getGameRepo() {
        return sessionService.getGameRepo();
    }

    public TimeLineItemRepository getTliRepo() {
        return sessionService.getTliRepo();
    }

    public Long getGameID() {
        return sessionService.getGameId();
    }

    public EntryRepository getEntryRepo() {
        return sessionService.getEntryRepo();
    }

    public void openTokenDialog(TimeLineItem item) {
        if (item.getPcId() != null) {
            Optional<PlayerCharacter> opc = sessionService.getPcRepo().findById(item.getPcId());
            if (opc.isPresent()) {
                tokenDialog.setPc(opc.get());
                tokenDialog.open();
            }
        }


    }

    public void updatePc(PlayerCharacter pc) {
        sessionService.getPcRepo().save(pc);
        Entry entry = new Entry();
        entry.setType(EventType.PCUpdate);
        entry.setPcId(pc.getId());
        entry.setGameId(getGameId());
        Broadcaster.broadcast(entry);
        removeAll();
        init();
    }
}
