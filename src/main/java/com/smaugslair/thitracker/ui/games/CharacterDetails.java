package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.abilities.Ability;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.AbilityScore;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.ui.games.tl.*;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class CharacterDetails extends RegisteredVerticalLayout {

    private final static Logger log = LoggerFactory.getLogger(CharacterDetails.class);

    private final GMTimeLineView gmTimeLineView;
    private final CacheService cacheService;
    private PlayerCharacter pc = null;
    private User user = null;
    private String color;
    private final DerivedInteger tsSpace = new DerivedInteger("Speed (spaces)", 0);
    private final DerivedDouble tsMPH = new DerivedDouble("Speed (MPH)", 0.0);

    public CharacterDetails(GMTimeLineView gmTimeLineView, CacheService cacheService) {
        this.gmTimeLineView = gmTimeLineView;
        this.cacheService = cacheService;
        setPadding(false);
        setSpacing(false);
        init();
    }

    public void setPc(PlayerCharacter pc, User user, String color) {
        this.pc = pc;
        this.user = user;
        this.color = color;
        removeAll();
        init();
    }

    private void init() {
        if (pc == null) {
            add(new Paragraph("Choose a Hero to view details"));
            return;
        }

        List<TraitRow> traitRows = new ArrayList<>(9);
        traitRows.add(new TraitRow(pc.getCharacterAndPlayerName(user), "w3-"+color));
        traitRows.add(new TraitRow(new ProgressionPoint(pc, this)));

        List<Trait> traits = pc.getTraits().stream().sorted().collect(Collectors.toList());

        traitRows.add(new TraitRow("Hero Traits"));
        traits.forEach(trait -> {
            if (trait.getType().equals(TraitType.Hero)) {
                traitRows.add(new TraitRow(new TraitPoint(trait, this)));
            }
        });

        traitRows.add(new TraitRow("Drama Traits"));
        traits.forEach(trait -> {
            if (trait.getType().equals(TraitType.Drama)) {
                traitRows.add(new TraitRow(new TraitPoint(trait, this)));
            }
        });

        Grid<TraitRow> grid = new Grid<>();
        grid.setThemeName("min-padding");
        grid.setHeightByRows(true);

        TraitRowFilter filter = new TraitRowFilter();
        filter.setShowDrama(false);
        filter.setShowHero(false);

        ListDataProvider<TraitRow> dataProvider = new ListDataProvider<>(traitRows);
        dataProvider.setFilter(filter::test);
        grid.setDataProvider(dataProvider);

        grid.addColumn(TraitRow::getName);//.setFlexGrow(2);
        grid.addComponentColumn(TraitRow::getComponent).setTextAlign(ColumnTextAlign.END);

        grid.setClassNameGenerator(item -> item.getColor());

        grid.addItemClickListener(event -> {
            switch (event.getItem().getName()) {
                case "Hero Traits":
                    filter.setShowHero(!filter.isShowHero());
                    dataProvider.refreshAll();
                    break;
                case "Drama Traits":
                    filter.setShowDrama(!filter.isShowDrama());
                    dataProvider.refreshAll();
                    break;
            }
        });

        add(grid);

        if (pc.getAbilityScores().isEmpty()) {
            List<Ability> abilities = cacheService.getAbilityRepository().findAll();
            abilities.forEach(ability -> {
                AbilityScore abilityScore = new AbilityScore();
                abilityScore.setPoints(ability.getBaseValue());
                abilityScore.setName(ability.getName());
                abilityScore.setPlayerCharacter(pc);
                abilityScore.setSortOrder(ability.getSortOrder());
                pc.getAbilityScores().put(abilityScore.getName(), abilityScore);
            });
        }



        List<AbilityRow> abilityRows = new ArrayList<>(8);

        abilityRows.add(0, new AbilityRow()); //Header row
        abilityRows.add(new AbilityRow(
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Perception"), this)),
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Stealth"), this))
        ));

        abilityRows.add(new AbilityRow(
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Aim"), this)),
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Dodge"), this))
        ));

        abilityRows.add(new AbilityRow(
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Strength"), this)),
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Toughness"), this))
        ));

        abilityRows.add(new AbilityRow(
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Influence"), this)),
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Self-Control"), this))
        ));

        abilityRows.add(new AbilityRow(
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Initiative"), this)),
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Movement"), this))
        ));

        abilityRows.add(new AbilityRow(
                new TraitRow("", ""),
                new TraitRow(new AbilityPoint(pc.getAbilityScores().get("Travel Mult"), this))
        ));

        abilityRows.add(new AbilityRow(
                new TraitRow("", ""),
                new TraitRow(tsSpace)
        ));
        abilityRows.add(new AbilityRow(
                new TraitRow("", ""),
                new TraitRow(tsMPH)
        ));

        calcSpeeds();

        AbilityRowFilter abilityRowFilter = new AbilityRowFilter();
        abilityRowFilter.setShowRows(false);

        ListDataProvider<AbilityRow> abilityRowListDataProvider = new ListDataProvider<>(abilityRows);
        abilityRowListDataProvider.setFilter(abilityRowFilter::test);
        grid.setDataProvider(dataProvider);

        Grid<AbilityRow> abilityRowGrid = new Grid<>();
        abilityRowGrid.setThemeName("min-padding");
        abilityRowGrid.setDataProvider(abilityRowListDataProvider);

        abilityRowGrid.addItemClickListener(event -> {
            if (event.getItem().isHeader()) {
                abilityRowFilter.setShowRows(!abilityRowFilter.isShowRows());
                abilityRowListDataProvider.refreshAll();
            }
        });

        abilityRowGrid.addColumn(AbilityRow::getName1).setFlexGrow(3);
        abilityRowGrid.addComponentColumn(AbilityRow::getComponent1).setFlexGrow(1);
        abilityRowGrid.addColumn(AbilityRow::getName2).setFlexGrow(3);
        abilityRowGrid.addComponentColumn(AbilityRow::getComponent2).setFlexGrow(1);

        abilityRowGrid.setHeightByRows(true);
        abilityRowGrid.getColumns().forEach(itemColumn -> {
            itemColumn.setAutoWidth(true);
        });

        abilityRowGrid.setClassNameGenerator(item -> item.getColor());
        add(abilityRowGrid);

    }

    private void calcSpeeds() {
        int travelSpeed = pc.getAbilityScores().get("Movement").getPoints()
                * pc.getAbilityScores().get("Travel Mult").getPoints();
        tsSpace.setValue(travelSpeed);
        tsMPH.setValue(travelSpeed*1.5);
    }

    public void updatePC() {
        gmTimeLineView.updatePc(pc);
        calcSpeeds();
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.PlayerPCUpdate.equals(entry.getType())) {
            if (pc != null && entry.getPcId().equals(pc.getId())) {
                removeAll();
                init();
            }
        }
    }
}
