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
import com.smaugslair.thitracker.util.RepoService;
import com.smaugslair.thitracker.util.SessionService;
import com.smaugslair.thitracker.websockets.TimelineBroadcaster;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
public class GMTimeLineView extends VerticalLayout {

    private static Logger log = LoggerFactory.getLogger(GMTimeLineView.class);

    private final RepoService repoService;
    private final SessionService sessionService;
    private final GMSession gmSession;


    public GMTimeLineView(GMSession gmSession, RepoService repoService, SessionService sessionService) {
        this.gmSession = gmSession;
        this.repoService = repoService;
        this.sessionService = sessionService;
        init();
    }


    public void refresh() {
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
            repoService.getEntryRepo().save(entry);
            gmSession.logAction(entry);

        }
        TimelineBroadcaster.broadcast(entry);
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

        grid.addComponentColumn((TimeLineItem item3) -> new NameField(item3, this)).setHeader("Character/NPC/Event");
        grid.addComponentColumn((TimeLineItem item3) -> new StunField(item3, this)).setHeader("Stun");
        grid.addComponentColumn((TimeLineItem item4) -> new ActionSelect(item4, this));
        grid.addComponentColumn((TimeLineItem item3) -> new TimeField(item3, this)).setHeader("Time");
        grid.addComponentColumn(ReactText::new).setHeader("React");
        Icon icon = VaadinIcon.EYE_SLASH.create();
        icon.setSize("16px");
        grid.addComponentColumn((TimeLineItem item3) -> new HiddenField(item3, this)).setHeader(icon).setTextAlign(ColumnTextAlign.CENTER);
        icon = VaadinIcon.CARET_UP.create();
        icon.setSize("16px");
        grid.addComponentColumn((TimeLineItem item2) -> new DeltaButton(item2, this)).setHeader(icon).setTextAlign(ColumnTextAlign.CENTER);
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
                refresh();
            });
        });

        contextMenu.addItem("Remove", event -> {
            event.getItem().ifPresent(item -> {
                getTliRepo().delete(item);
                refresh();
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
                repoService.getCiRepo().save(collectedItem);
                Notification.show("Saved "+collectedItem.getName()+" to your collection",
                        2000, Notification.Position.TOP_CENTER);
            });
        });

        add(grid);

        User user = SecurityUtils.getLoggedInUser();

        List<Friendship> friendships = repoService.getFriendsRepo().findAllByUserOrFriend(user, user);

        Set<Integer> userIds = new HashSet<>();
        friendships.forEach(friendship -> {
            userIds.add(friendship.getUser().getId());
            userIds.add(friendship.getFriend().getId());
        });

        List<PlayerCharacter> pcs = new ArrayList<>();
        userIds.forEach(id -> {
            pcs.addAll(repoService.getPcRepo().findAllByUserIdAndGameIdIsNull(id));
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
                repoService.getPcRepo().save(pc);
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
            refresh();
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
            refresh();
        });
        gameActions.add(resetStun);


        Button resetTime = new Button("Reset time");
        resetTime.addClickListener(event -> {
            Iterable<TimeLineItem> timeLineItems = getTliRepo().findByGameIdOrderByTime(getGameId());
            timeLineItems.forEach(item -> item.setTime(0));
            getTliRepo().saveAll(timeLineItems);
            game.setLastEventId(null);
            getGameRepo().save(game);
            refresh();
        });
        gameActions.add(resetTime);


        List<CollectedItem> collectedItems = repoService.getCiRepo().findAllByGmId(SecurityUtils.getLoggedInUser().getId());
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
        return repoService.getGameRepo();
    }

    public TimeLineItemRepository getTliRepo() {
        return repoService.getTliRepo();
    }

    public Long getGameID() {
        return sessionService.getGameId();
    }

    public EntryRepository getEntryRepo() {
        return repoService.getEntryRepo();
    }
}
