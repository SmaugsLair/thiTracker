package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.HeroPower;
import com.smaugslair.thitracker.data.pc.HeroPowerMod;
import com.smaugslair.thitracker.data.pc.HeroPowerSet;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerMod;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class PowerSetEditor extends VerticalLayout {

    private final static Logger log = LoggerFactory.getLogger(PowerSetEditor.class);

    private PlayerCharacter pc;
    private final SessionService sessionService;
    private final CharacterSheet characterSheet;
    private HeroPowerSet heroPowerSet;
    private List<HeroPower> heroPowers;
    Map<Integer, SortedSet<Power>> powerMap;

    private Integer currentTier = 1;

    public PowerSetEditor(CharacterSheet characterSheet, SessionService sessionService) {
        this.sessionService = sessionService;
        this.characterSheet = characterSheet;
        //init();
    }

    public void start(PlayerCharacter pc, HeroPowerSet heroPowerSet, List<HeroPower> heroPowers) {
        this.pc = pc;
        this.heroPowerSet = heroPowerSet;
        this.heroPowers = heroPowers;
        powerMap = sessionService.getPowersCache().getPowersMap().get(heroPowerSet.getPowerSet().getName());

        init();
    }

    private void init () {
        removeAll();

        //List<HeroPower> heroPowers = sessionService.getHpRepo().findAllByPlayerCharacter(pc);

        //Map<Integer, SortedSet<Power>>
        Map<Integer, SortedSet<PowerTarget>> powerTargetMap = new TreeMap<>();
        powerMap.forEach((tier, powers) -> {
            powerTargetMap.put(tier, new TreeSet<>());
            powers.forEach(power -> {
                AvailableTaken at = determineAvailability(heroPowerSet, power, heroPowers, tier);
                powerTargetMap.get(tier).add(new PowerTarget(power, tier, at.isAvailable(), at.getTimesTaken(), heroPowerSet));
            });

        });


        String psName = heroPowerSet.getPowerSet().getName();
        add(new Span("Choosing "+psName+" powers for " + characterSheet.getCharacterName()));

        add(new Button("Remove "+psName+ " power set", event -> {
            characterSheet.removeHeroPowerSet(heroPowerSet);
        }));
/*
        for (HeroPower heroPower: heroPowers) {
            if (heroPower.getHeroPowerSet().equals(heroPowerSet)) {
                HorizontalLayout powerRow = new HorizontalLayout(new Span("Power: "+heroPower.getPower().getName()));
                powerRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                powerRow.add(new Span(AbilityModsRenderer.renderAmString(heroPower.getMods().values())));

                powerRow.add(new Button("Remove", event -> {
                    removePower(heroPower);
                }));
                add(powerRow);
            }
        }*/
        Tabs tabs = new Tabs();
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.setFlexGrowForEnclosedTabs(0);
        add(tabs);

        VerticalLayout tabTarget = new VerticalLayout();
        add(tabTarget);

        Map<Tab, PowerTargetGrid> tabMap = new HashMap<>();


        //VerticalLayout powersLayout = new VerticalLayout();
        //powersLayout.setWidthFull();
        powerTargetMap.forEach((tier, powerCollection) -> {
            PowerTargetGrid grid = new PowerTargetGrid(tier, powerCollection, this);

            Tab tab = new Tab(new Span("Tier "+tier + ": " + grid.getTaken()));
            tabs.add(tab);
            tabMap.put(tab, grid);
            if (tier == currentTier) {
                tabTarget.add(grid);
                tabs.setSelectedTab(tab);
            }
        });
        tabs.addSelectedChangeListener(event -> {
            tabTarget.removeAll();
            PowerTargetGrid ptGrid = tabMap.get(event.getSelectedTab());
            currentTier = ptGrid.getTier();
            tabTarget.add(ptGrid);
        });
        //add(powersLayout);
    }


    private AvailableTaken determineAvailability(HeroPowerSet heroPowerSet, Power power, List<HeroPower> heroPowers, Integer tier) {


        AvailableTaken availableTaken = new AvailableTaken();
        Map<Power, List<HeroPower>> map = new HashMap<>();
        int powersInSet = 0;
        for (HeroPower heroPower: heroPowers) {
            if (heroPower.getHeroPowerSet().equals(heroPowerSet)) {
                ++powersInSet;
            }
            List<HeroPower> list = map.get(heroPower.getPower());
            if (list == null) {
                list = new ArrayList<>();
                map.put(heroPower.getPower(), list);
            }
            list.add(heroPower);
        }
        if (tier > powersInSet + 1) {
            return availableTaken;
        }
        List<HeroPower> heroPowerList = map.get(power);
        if (heroPowerList != null && !heroPowerList.isEmpty()) {
            availableTaken.setTimesTaken(heroPowerList.size());
            if (heroPowerList.size() >= Integer.valueOf(power.getMaxTaken())) {
                return availableTaken;
            }
        }
        availableTaken.setAvailable(power.prerequsitesMet(heroPowers.stream().map(HeroPower::getName).collect(Collectors.toList())));
        return availableTaken;
    }



    public void addPowerTarget(PowerTarget powerTarget) {

        Power power = powerTarget.getPower();

        HeroPower heroPower = new HeroPower();
        heroPower.setPower(power);
        heroPower.setHeroPowerSet(powerTarget.getHeroPowerSet());
        heroPower.setPlayerCharacter(pc);
        heroPower.setTier(powerTarget.getTier());
        heroPowers.add(heroPower);

        if (!power.getPowerMods().isEmpty()) {
            if (power.getPowerMods().containsKey(Ability.Choice)) {
                Dialog choiceDialog = new AbilityChoiceDialog(heroPower, this::powerModChoices,
                        power.getPowerMods().get(Ability.Choice).getValue());
                choiceDialog.open();
            }
            else {
                for (PowerMod mod : power.getPowerMods().values()) {
                    HeroPowerMod newMod = new HeroPowerMod();
                    newMod.setAbility(mod.getAbility());
                    newMod.setValue(mod.getValue());
                    newMod.setHeroPower(heroPower);
                    heroPower.getMods().put(mod.getAbility(), newMod);
                }
            }
        }
        init();
    }

    private void powerModChoices(HeroPower heroPower) {
        for (PowerMod mod : heroPower.getPower().getPowerMods().values()) {
            if (!(mod.getAbility() == Ability.Choice)) {
                heroPower.addMod(mod.getAbility(), mod.getValue());
            }
        }
        removeAll();
        init();
    }

    public void removePowerTarget(PowerTarget powerTarget) {
        log.info("removePowerTarget: "+powerTarget);
        //List<HeroPower> heroPowers = sessionService.getHpRepo().findAllByPlayerCharacter(pc);

        HeroPower heroPower = null;
        //Scan though whole list, hang on to the last match
        for (HeroPower hp : heroPowers) {
            if (hp.getPower().equals(powerTarget.getPower())) {
                heroPower = hp;
            }
        }
        log.info("removing heroPower "+heroPower);
        log.info("before size "+heroPowers.size());

        heroPowers.remove(heroPower);
        log.info("after size "+heroPowers.size());

        init();
    }

    public List<HeroPower> getHeroPowers() {
        return heroPowers;
    }

    public List<String> getSatisfiedPrereqs() {

        List<String> list = new ArrayList<>();
        for (HeroPower heroPower : heroPowers) {
            list.addAll(heroPower.getPower().getPrereqList());
        }
        return list;

    }

    public SortedSet<Power> getAllPowers() {
        return sessionService.getPowersCache().getPowers();
    }
}
