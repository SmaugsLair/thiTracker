package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.atd.ActionTime;
import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.game.*;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedDelta;
import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.ci.ImportButton;
import com.smaugslair.thitracker.ui.tl.*;
import com.smaugslair.thitracker.util.AtdCache;
import com.smaugslair.thitracker.util.ColorCollection;
import com.smaugslair.thitracker.util.SessionService;
import com.smaugslair.thitracker.websockets.TimelineBroadcaster;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
public class GMTimeLineView extends VerticalLayout {

    private static Logger log = LoggerFactory.getLogger(GMTimeLineView.class);

    private final CollectedItemRepository ciRepo;
    private final GameRepository gameRepo;
    private final TimeLineItemRepository tliRepo;
    private final PlayerCharacterRepository pcRepo;
    private final SessionService sessionService;
    private final EntryRepository entryRepository;


    public GMTimeLineView(GameRepository gameRepository,
                          TimeLineItemRepository tliRepository,
                          CollectedItemRepository collectedItemRepository,
                          PlayerCharacterRepository playerCharacterRepository,
                          SessionService sessionService,
                          EntryRepository entryRepository) {
        gameRepo = gameRepository;
        tliRepo = tliRepository;
        ciRepo = collectedItemRepository;
        pcRepo = playerCharacterRepository;
        this.sessionService = sessionService;
        this.entryRepository = entryRepository;
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
            entryRepository.save(entry);
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

        final Game game = gameRepo.findById(getGameId()).orElse(new Game());

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

        List<TimeLineItem> items = tliRepo.findByGameIdOrderByTime(game.getId());

        TimeLineItem lastEvent = null;
        if (game.getLastEventId() != null) {
            lastEvent = tliRepo.findById(game.getLastEventId()).orElse(null);
        }

        //log.info("Loaded "+items.size()+" events");
        //List<ActionTimeDefault> atds = atdRepository.findAll();
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
            //log.info("actionTimes: "+item.getActionTimes());
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
                tliRepo.save(copy);
                refresh();
            });
        });

        contextMenu.addItem("Remove", event -> {
            event.getItem().ifPresent(item -> {
                tliRepo.delete(item);
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
                ciRepo.save(collectedItem);
                Notification.show("Saved "+collectedItem.getName()+" to your collection",
                        2000, Notification.Position.TOP_CENTER);
            });
        });

        add(grid);


        Dialog addItemDialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("PC", "Other");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);


        Div radioFields = new Div();
        Select<PlayerCharacter> pcField = new Select<>();
        pcField.setItemLabelGenerator(PlayerCharacter::getName);

        TextField otherField = new TextField();
        otherField.setPlaceholder("Other");

        List<PlayerCharacter> pcs = pcRepo.findAllByUserIdAndGameIdIsNull(SecurityUtils.getLoggedInUser().getId());
        pcField.setItems(pcs);

        if (pcs.isEmpty()) {
            radioGroup.setValue("Other");
            radioFields.add(otherField);
            radioGroup.setItemEnabledProvider(item -> !"PC".equals(item));
        }
        else {
            radioGroup.setValue("PC");
            radioFields.add(pcField);
        }


        radioGroup.addValueChangeListener(event -> {
            if (event.getValue() == "PC") {
                radioFields.remove(otherField);
                radioFields.add(pcField);
            }
            else {
                radioFields.remove(pcField);
                radioFields.add(otherField);
            }
        });
        formLayout.addFormItem(radioFields, radioGroup);

        IntegerField stunField = new IntegerField();
        stunField.setHasControls(true);
        stunField.setValue(0);
        stunField.setMin(0);
        formLayout.addFormItem(stunField, "Stun");
        IntegerField timeField = new IntegerField();
        timeField.setHasControls(true);
        timeField.setValue(0);
        formLayout.addFormItem(timeField, "Time");
        Checkbox hidden = new Checkbox();
        hidden.setValue(true);
        formLayout.addFormItem(hidden, "Hidden");
        addItemDialog.add(formLayout);

        Button confirmButton = new Button("Add event to "+game.getName(), event -> {
            TimeLineItem item = new TimeLineItem();
            if (radioGroup.getValue().equals("PC")) {
                PlayerCharacter pc = pcField.getValue();
                item.setName(pc.getName());
                item.setPcId(pc.getId());
                pc.setGameId(getGameId());
                pcRepo.save(pc);
            }
            else {
                item.setName(otherField.getValue());
            }
            item.setStun(stunField.getValue());
            item.setTime(timeField.getValue());
            item.setHidden(hidden.getValue());
            item.setGameId(game.getId());
            item.initializeDeltas();
            tliRepo.save(item);
            addItemDialog.close();
            refresh();
        });
        addItemDialog.add(confirmButton);
        Button cancelButton = new Button("Cancel", event -> {
            addItemDialog.close();
        });
        addItemDialog.add(cancelButton);
        addItemDialog.setWidth("500px");


        Button addButton = new Button("Add event");
        addButton.addClickListener(event -> addItemDialog.open());
        gameActions.add(addButton);

        Button resetStun = new Button("Reset stun");
        resetStun.addClickListener(event -> {
            Iterable<TimeLineItem> timeLineItems = tliRepo.findByGameIdOrderByTime(getGameId());
            timeLineItems.forEach(item -> item.setStun(0));
            tliRepo.saveAll(timeLineItems);
            refresh();
        });
        gameActions.add(resetStun);


        Button resetTime = new Button("Reset time");
        resetTime.addClickListener(event -> {
            Iterable<TimeLineItem> timeLineItems = tliRepo.findByGameIdOrderByTime(getGameId());
            timeLineItems.forEach(item -> item.setTime(0));
            tliRepo.saveAll(timeLineItems);
            game.setLastEventId(null);
            gameRepo.save(game);
            refresh();
        });
        gameActions.add(resetTime);


        List<CollectedItem> collectedItems = ciRepo.findAllByGmId(SecurityUtils.getLoggedInUser().getId());
        if (!collectedItems.isEmpty()) {

            Dialog importDialog = new Dialog();
            importDialog.setWidth("500px");
            Grid<CollectedItem> ciGrid = new Grid<>();
            ciGrid.setItems(collectedItems);
            ciGrid.setHeightByRows(true);
            ciGrid.setClassNameGenerator(item -> "w3-"+item.getColor());
            ciGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
            ciGrid.addColumn(CollectedItem::getName);
            ciGrid.addComponentColumn((CollectedItem item1) -> new ImportButton(item1, this));
            importDialog.add(ciGrid);

            Button cancelImportButton = new Button("Cancel", event -> {
                importDialog.close();
            });
            importDialog.add(cancelImportButton);

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
            Game game = gameRepo.findById(item.getGameId()).orElse(null);
            if (game != null) {
                game.setLastEventId(item.getId());
                gameRepo.save(game);
            }
            StringBuffer sb = new StringBuffer(item.getName()).append(" spent ").append(item.getActionTime().time);
            sb.append(" TU on a ").append(item.getActionTime().name).append(" action");
            logText = sb.toString();
        }
        tliRepo.save(item);
        refreshWithLog(logText);
    }

    public GameRepository getGameRepo() {
        return gameRepo;
    }
    public TimeLineItemRepository getTlRepo() {
        return tliRepo;
    }

    public Long getGameID() {
        return sessionService.getGameId();
    }
}
