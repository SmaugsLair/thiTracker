package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.*;
import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.data.powers.PowerSetMod;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.MultiColumnLayout;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
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
    private boolean editablePowers;
    private boolean editingPowers = false;
    private final SessionService sessionService;
    private PlayerCharacter pc = null;
    private String color;
    private DerivedField tsSpace;
    private DerivedField tsMPH;
    private PowerSetEditor powerSetEditor;
    private ConfirmDialog editorDialog;

    private boolean heroExpanded = false;
    private boolean dramaExpanded = false;
    private boolean abilitiesExpanded = false;

    public CharacterSheet(Function<PlayerCharacter, PlayerCharacter> pcUpdater,
                          boolean editablePowers,
                          SessionService sessionService) {
        this.pcUpdater = pcUpdater;
        this.editablePowers = editablePowers;
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
        init();
    }

    private void init() {
        removeAll();
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
            Button button = new UserSafeButton("+", event -> {
                Trait trait = new Trait();
                trait.setType(TraitType.Drama);
                trait.setPoints(0);
                trait.setName("Drama Trait "+newDrama);
                trait.setSortOrder(sortOrder.get()+1);
                trait.setPlayerCharacter(pc);
                pc.getTraits().add(trait);
                updatePC();
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
                if (ability.isMapped() && pc.getAbilityScores().get(ability) == null) {
                    //log.info("Adding "+ability+ " score to "+pc.getName());
                    AbilityScore abilityScore = new AbilityScore(ability, pc);
                    //abilityScore.setAbility(ability);
                    //abilityScore.setBase(ability.getBaseValue());
                    //abilityScore.setMods(0);
                    //abilityScore.setPlayerCharacter(pc);
                    pc.getAbilityScores().put(ability, abilityScore);
                }
            }
        }

        /*log.info("Char sheet, ability score count: "+pc.getAbilityScores().size());
        for (AbilityScore ac :pc.getAbilityScores().values() ) {
            log.info(ac.toString());
        }*/

        List<AbilityRow> abilityRows = new ArrayList<>(8);

        abilityRows.add(0, new AbilityRow()); //Header row
        for (int i = 0; i < 6; ++i) {
            //log.info("Char sheet, creating row "+i);
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

        List<HeroPowerSet> heroPowerSets = sessionService.getHpsRepo().findAllByPlayerCharacter(pc);
        List<HeroPower> heroPowers = sessionService.getHpRepo().findAllByPlayerCharacter(pc);

        StringBuilder sb = new StringBuilder("Powers: ");
        for (HeroPowerSet powerSet : heroPowerSets) {
            sb.append(powerSet.getPowerSet().getName()).append("[");
            int count = 0;
            for (HeroPower power : heroPowers) {
                if (power.getHeroPowerSet().equals(powerSet)) {
                    ++count;
                }
            }
            sb.append(count).append("] ");
        }
        HorizontalLayout heroPowersLayout = new HorizontalLayout();
        //heroPowersLayout.setWidthFull();
        int needed = 2;
        for (HeroPowerSet powerSet : heroPowerSets) {
            heroPowersLayout.add(new PowerSetLayout(powerSet, heroPowers, this));
            --needed;
        }
        while (needed > 0) {
            heroPowersLayout.add(new Button("Choose power set", startEvent -> {
                openPowerSetDialog(heroPowerSets);
            }));
            --needed;
        }
        add(heroPowersLayout);
        /*
        VerticalLayout powerLayout = new VerticalLayout();
        if (editingPowers) {
            powerLayout.add(new UserSafeButton("Done editing", event -> {
                editingPowers = false;
                removeAll();
                init();
            }));
            Details powerDetails = new Details(sb.toString(), new PowersList(pc, sessionService, this));
            powerLayout.add(powerDetails);
            powerDetails.setOpened(true);
        }
        else {
            powerLayout.add(new UserSafeButton("Edit Powers", event -> {
                editingPowers = true;
                removeAll();
                init();
            }));
            powerLayout.add(new HeroPowersLayout( heroPowerSets, heroPowers, this));
        }
        add(powerLayout);*/


        powerSetEditor = new PowerSetEditor(this, sessionService);

    }

    private void calcSpeeds() {
        int travelSpeed = pc.getAbilityScores().get(Ability.Movement).getPoints()
                * pc.getAbilityScores().get(Ability.TravelMult).getPoints();
        tsSpace.getSpan().setText(String.valueOf(travelSpeed));
        tsMPH.getSpan().setText(String.valueOf(travelSpeed*1.5));
    }

    public void updatePC() {
        log.info("updating pc");
        pc = pcUpdater.apply(pc);
        calcSpeeds();
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.PCUpdate.equals(entry.getType())) {
            if (pc != null && entry.getPcId().equals(pc.getId())) {
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
        init();
    }

    public void showPowerChoiceDialog(HeroPowerSet powerSet) {
        editorDialog = new ConfirmDialog(powerSetEditor);
        List<HeroPower> starting = sessionService.getHpRepo().findAllByPlayerCharacter(pc);
        powerSetEditor.start(pc, powerSet, new ArrayList<>(starting));
        editorDialog.open();
        editorDialog.setConfirmButton(new UserSafeButton("Save", buttonClickEvent -> {

            List<HeroPower> edited = powerSetEditor.getHeroPowers();

            Collection<HeroPower> intersection = CollectionUtils.intersection(starting, edited);
            Collection<HeroPower> disjunction = CollectionUtils.disjunction(starting, edited);

            //log.info("updating all: "+intersection);
            //sessionService.getHpRepo().saveAll(intersection);


            disjunction.forEach(heroPower -> {
                if (heroPower.getId() == null) {
                    //log.info("saving new: "+heroPower);
                    sessionService.getHpRepo().save(heroPower);
                }
                else {
                    //log.info("deleting old : "+heroPower);
                    sessionService.getHpRepo().delete(heroPower);
                }
            });

            calculateAbilityScores();
            updatePC();
            editorDialog.close();
            init();
        }));
    }


    private void openPowerSetDialog(List<HeroPowerSet> heroPowerSetList) {
        List<PowerSet> excludes = new ArrayList<>();
        if (heroPowerSetList != null) {
            heroPowerSetList.forEach(heroPowerSet -> {
                excludes.add(heroPowerSet.getPowerSet());
            });
        }

        MultiColumnLayout multiColumnLayout = new MultiColumnLayout(3);

        Dialog dialog = new Dialog(multiColumnLayout);
        SortedSet<PowerSet> powerSetList = sessionService.getPowersCache().getPowerSetList();
        powerSetList.forEach(powerSet -> {
            if (!excludes.contains(powerSet)) {
                multiColumnLayout.addToNextColumn(new Button(powerSet.getName(), chooseEvent -> {
                    choosePowerSet(powerSet);
                    dialog.close();
                }));
            }
        });
        dialog.open();
    }


    private void choosePowerSet(PowerSet powerSet) {
        HeroPowerSet heroPowerSet = new HeroPowerSet();
        heroPowerSet.setPowerSet(powerSet);
        heroPowerSet.setPlayerCharacter(pc);
        if (!powerSet.getPowerSetMods().isEmpty()) {
            if (powerSet.getPowerSetMods().containsKey(Ability.Choice)) {
                Dialog choiceDialog = new AbilityChoiceDialog(heroPowerSet, this::powerSetModChoices,
                        powerSet.getPowerSetMods().get(Ability.Choice).getValue());
                choiceDialog.open();
            }
            else {
                for (PowerSetMod mod : powerSet.getPowerSetMods().values()) {
                    HeroPowerSetMod newMod = new HeroPowerSetMod();
                    newMod.setAbility(mod.getAbility());
                    newMod.setValue(mod.getValue());
                    newMod.setHeroPowerSet(heroPowerSet);
                    heroPowerSet.getMods().put(mod.getAbility(),newMod);
                }
            }
        }
        sessionService.getHpsRepo().save(heroPowerSet);
        calculateAbilityScores();
        updatePC();
        init();
    }

    public void removeHeroPowerSet(HeroPowerSet heroPowerSet) {
        List<HeroPower> heroPowers = sessionService.getHpRepo().findAllByPlayerCharacter(pc);
        for (HeroPower hp : heroPowers) {
            if (hp.getHeroPowerSet().equals(heroPowerSet)) {
                sessionService.getHpRepo().delete(hp);
            }
        }
        sessionService.getHpsRepo().delete(heroPowerSet);
        calculateAbilityScores();
        updatePC();
        editorDialog.close();
        init();

    }

    private void calculateAbilityScores() {
        pc.getAbilityScores().forEach((ability, abilityScore) -> abilityScore.reset());
        List<HeroPowerSet> heroPowerSets = sessionService.getHpsRepo().findAllByPlayerCharacter(pc);
        heroPowerSets.forEach(heroPowerSet -> {
            heroPowerSet.getMods().forEach((ability, heroPowerSetMod) -> {
                pc.getAbilityScores().get(ability).adjustMods(heroPowerSetMod.getValue());
            });
        });
        List<HeroPower> heroPowers = sessionService.getHpRepo().findAllByPlayerCharacter(pc);
        heroPowers.forEach(heroPower -> {
            heroPower.getMods().forEach((ability, heroPowerMod) -> {
                pc.getAbilityScores().get(ability).adjustMods(heroPowerMod.getValue());
            });
        });
    }

    private void powerSetModChoices(HeroPowerSet heroPowerSet) {
        for (PowerSetMod mod : heroPowerSet.getPowerSet().getPowerSetMods().values()) {
            if (!(mod.getAbility() == Ability.Choice)) {
                heroPowerSet.addMod(mod.getAbility(), mod.getValue());
            }
        }
        sessionService.getHpsRepo().save(heroPowerSet);
        calculateAbilityScores();
        updatePC();
        init();
    }

}
