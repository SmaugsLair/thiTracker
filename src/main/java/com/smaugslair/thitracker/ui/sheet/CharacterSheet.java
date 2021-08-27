package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.AbilityScore;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class CharacterSheet extends RegisteredVerticalLayout {

    private final static Logger log = LoggerFactory.getLogger(CharacterSheet.class);
/*
    private final static String[][] ABILITY_LAYOUT = {
            {"Perception", "Stealth"},
            {"Aim", "Dodge"},
            {"Strength", "Toughness"},
            {"Influence", "Self-Control"},
            {"Initiative", "Movement"},
            {"", "Travel Mult"}
    };*/

    private final static int MAX_DRAMA = 10;

    private final Function<PlayerCharacter, PlayerCharacter> pcUpdater;
    private final SessionService sessionService;
    private PlayerCharacter pc = null;
    private String color;
    private DerivedField tsSpace;
    private DerivedField tsMPH;

    private boolean heroExpanded = false;
    private boolean dramaExpanded = false;
    private boolean abilitiesExpanded = false;

    public CharacterSheet(Function<PlayerCharacter, PlayerCharacter> pcUpdater,
                          SessionService sessionService) {
        this.pcUpdater = pcUpdater;
        this.sessionService = sessionService;
        setPadding(false);
        setSpacing(false);
        init();
    }

    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
        color = "";
        if (pc != null && pc.getGameId() != null) {
            //NameValue nameValue = new NameValue("gameId", pc.getGameId());
            //Loading the whole list so that the cache is not loaded with only a single item
            //List<TimeLineItem> items = cacheService.getTliCache().findManyByProperty(nameValue);
            List<TimeLineItem> items = sessionService.getTliRepo().findByGameId(pc.getGameId());
            for (TimeLineItem item : items) {
                if (pc.getId().equals(item.getPcId())) {
                    color = item.getColor();
                    break;
                }
            }
        }
        removeAll();
        init();
    }

    private void init() {
        if (pc == null) {
            add(new Paragraph("Choose a Hero to view details"));
            return;
        }
        User user = sessionService.getUserRepository().findById(pc.getUserId()).orElse(new User());
        List<TraitRow> traitRows = new ArrayList<>(9);
        traitRows.add(new MetaRow(pc.getName(), user.getDisplayName(), color));
        traitRows.add(new CivilianName(pc, this));
        traitRows.add(new ProgressionPoint(pc, this));

        List<Trait> traits = pc.getTraits().stream().sorted().collect(Collectors.toList());

        AtomicInteger dramaCount = new AtomicInteger();
        AtomicInteger sortOrder = new AtomicInteger();

        traitRows.add(new MetaRow("Hero Traits"));
        traits.forEach(trait -> {
            if (trait.getType().equals(TraitType.Hero)) {
                traitRows.add(new TraitField(trait, this));
            }
            else {
                dramaCount.incrementAndGet();
                sortOrder.set(trait.getSortOrder());
            }
        });

        int newDrama = dramaCount.get() + 1;

        traitRows.add(new MetaRow("Drama Traits"));
        traits.forEach(trait -> {
            if (trait.getType().equals(TraitType.Drama)) {
                traitRows.add(new TraitField(trait, this));
            }
        });
        if (dramaCount.get() < MAX_DRAMA) {
            Button button = new Button("+", event -> {
                Trait trait = new Trait();
                trait.setType(TraitType.Drama);
                trait.setPoints(0);
                trait.setName("Drama Trait "+newDrama);
                trait.setSortOrder(sortOrder.get()+1);
                trait.setPlayerCharacter(pc);
                pc.getTraits().add(trait);
                updatePC();
                removeAll();
                init();
            });
            traitRows.add(new MetaRow(button, TraitType.Drama));
        }

        Grid<TraitRow> grid = new Grid<>();
        grid.setThemeName("min-padding");
        grid.setHeightByRows(true);

        TraitRowFilter filter = new TraitRowFilter();
        filter.setShowDrama(dramaExpanded);
        filter.setShowHero(heroExpanded);

        ListDataProvider<TraitRow> dataProvider = new ListDataProvider<>(traitRows);
        dataProvider.setFilter(filter::test);
        grid.setDataProvider(dataProvider);

        grid.addComponentColumn(TraitRow::getLabel);//.setFlexGrow(2);
        grid.addComponentColumn(TraitRow::getComponent);//.setTextAlign(ColumnTextAlign.END);

        grid.setClassNameGenerator(item -> item.getColor());

        grid.addItemClickListener(event -> {
            switch (event.getItem().getLabelValue()) {
                case "Hero Traits":
                    heroExpanded = !heroExpanded;
                    filter.setShowHero(heroExpanded);
                    dataProvider.refreshAll();
                    break;
                case "Drama Traits":
                    dramaExpanded = !dramaExpanded;
                    filter.setShowDrama(dramaExpanded);
                    dataProvider.refreshAll();
                    break;
            }
        });

        add(grid);

        if (pc.getAbilityScores().isEmpty()) {
            for (Ability ability : Ability.values()) {
                AbilityScore abilityScore = new AbilityScore();
                abilityScore.setAbility(ability);
                abilityScore.setPoints(ability.getBaseValue());
                abilityScore.setPlayerCharacter(pc);
                pc.getAbilityScores().put(ability, abilityScore);
            }
        }

        List<AbilityRow> abilityRows = new ArrayList<>(8);

        abilityRows.add(0, new AbilityRow()); //Header row
        for (int i = 0; i < 6; ++i) {
            abilityRows.add(new AbilityRow(
                    pc.getAbilityScores().get(Ability.getAt(i, 0)),
                    pc.getAbilityScores().get(Ability.getAt(i, 1)), this
            ));
        }

        tsSpace = new DerivedField("Speed (spaces)", "0");
        tsMPH = new DerivedField("Speed (MPH)", "0.0");

        abilityRows.add(new AbilityRow(tsSpace));
        abilityRows.add(new AbilityRow(tsMPH));

        calcSpeeds();

        AbilityRowFilter abilityRowFilter = new AbilityRowFilter();
        abilityRowFilter.setShowRows(abilitiesExpanded);

        ListDataProvider<AbilityRow> abilityRowListDataProvider = new ListDataProvider<>(abilityRows);
        abilityRowListDataProvider.setFilter(abilityRowFilter::test);
        grid.setDataProvider(dataProvider);

        Grid<AbilityRow> abilityRowGrid = new Grid<>();
        abilityRowGrid.setThemeName("min-padding");
        abilityRowGrid.setDataProvider(abilityRowListDataProvider);

        abilityRowGrid.addItemClickListener(event -> {
            if (event.getItem().isHeader()) {
                abilitiesExpanded = !abilitiesExpanded;
                abilityRowFilter.setShowRows(abilitiesExpanded);
                abilityRowListDataProvider.refreshAll();
            }
        });

        abilityRowGrid.addColumn(AbilityRow::getLabel1).setFlexGrow(3);
        abilityRowGrid.addComponentColumn(AbilityRow::getComponent1).setFlexGrow(1);
        abilityRowGrid.addColumn(AbilityRow::getLabel2).setFlexGrow(3);
        abilityRowGrid.addComponentColumn(AbilityRow::getComponent2).setFlexGrow(1);

        abilityRowGrid.setHeightByRows(true);
        abilityRowGrid.getColumns().forEach(itemColumn -> {
            itemColumn.setAutoWidth(true);
        });

        abilityRowGrid.setClassNameGenerator(item -> item.getColor());
        add(abilityRowGrid);

    }

    private void calcSpeeds() {
        int travelSpeed = pc.getAbilityScores().get(Ability.Movement).getPoints()
                * pc.getAbilityScores().get(Ability.TravelMult).getPoints();
        tsSpace.getSpan().setText(String.valueOf(travelSpeed));
        tsMPH.getSpan().setText(String.valueOf(travelSpeed*1.5));
    }

    public void updatePC() {
        pc = pcUpdater.apply(pc);
        calcSpeeds();
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.PCUpdate.equals(entry.getType())) {
            if (pc != null && entry.getPcId().equals(pc.getId())) {
                removeAll();
                sessionService.getPcRepo().findById(pc.getId()).ifPresent(playerCharacter -> {
                    pc = playerCharacter;
                });
                init();
            }
        }
    }

    public void removeTrait(Trait trait) {
        pc.getTraits().remove(trait);
        updatePC();
        removeAll();
        init();
    }
}
