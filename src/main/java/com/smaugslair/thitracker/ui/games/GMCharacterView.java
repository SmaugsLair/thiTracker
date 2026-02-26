package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.*;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.ui.players.PCUpdater;
import com.smaugslair.thitracker.ui.sheet.CharacterEditor;
import com.smaugslair.thitracker.ui.sheet.ProgressionPoint;
import com.smaugslair.thitracker.ui.sheet.TraitField;
import com.smaugslair.thitracker.util.BeanFinder;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Paragraph;

import java.util.List;
import java.util.stream.Collectors;

//@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class GMCharacterView extends RegisteredVerticalLayout implements CharacterEditor {

    //private final static Logger log = LoggerFactory.getLogger(GMCharacterView.class);

    private PCUpdater pcUpdater;
    //private SessionService sessionService;
    private PlayerCharacter pc = null;
    private String color = "";


    public GMCharacterView() {
        setPadding(false);
        setSpacing(false);
        //log.info("Character view created");
    }

    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
        color = "";
        if (pc != null && pc.getGameId() != null) {
            //NameValue nameValue = new NameValue("gameId", pc.getGameId());
            //Loading the whole list so that the cache is not loaded with only a single item
            //List<TimeLineItem> items = cacheService.getTliCache().findManyByProperty(nameValue);
            List<TimeLineItem> items = BeanFinder.getBean(TimeLineItemRepository.class).findByGameId(pc.getGameId());
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
        User user = BeanFinder.getBean(UserRepository.class).findById(pc.getUserId()).orElse(new User());
        //List<TraitRow> traitRows = new ArrayList<>(9);
        PairedComponent nameComponent = new PairedComponent(pc.getName(), user.getDisplayName());
        nameComponent.setClassName(color);
        add(nameComponent);
        //traitRows.add(new MetaRow(pc.getName(), user.getDisplayName(), color));
        add(new PairedComponent("aka", pc.getCivilianId()));
        //traitRows.add(new CivilianName(pc, null));
        //add(new PairedComponent("Prog points", pc.getProgressionTokens().toString()));
        ProgressionPoint pp = new ProgressionPoint(pc, this);
        add(new PairedComponent(pp.getLeft(), pp.getRight()));
        //traitRows.add(new ProgressionPoint(pc, null));

        List<Trait> traits = pc.getTraits().stream()
                .filter(trait -> trait.getType().equals(TraitType.Hero))
                .sorted()
                .collect(Collectors.toList());
        add("Hero Traits");
        traits.forEach(trait -> {
            TraitField traitField = new TraitField(trait, false, this);
            add(new PairedComponent(traitField.getLeft(), traitField.getRight()));
        });

        traits = pc.getTraits().stream()
                .filter(trait -> trait.getType().equals(TraitType.Drama))
                .sorted()
                .toList();
        add("Drama Traits");
        traits.forEach(trait ->{
            TraitField traitField = new TraitField(trait, false, this);
            add(new PairedComponent(traitField.getLeft(), traitField.getRight()));
        });

        add("Ability Scores");
        for (int i = 0; i <= 4; ++i) {
            //log.info("Char sheet, creating row "+i);
            AbilityScore as0 = pc.getAbilityScores().get(Ability.getAt(i, 0));
            AbilityScore as1 = pc.getAbilityScores().get(Ability.getAt(i, 1));
            add(new PairedComponent(as0.getText(), as1.getText()));
        }

        List<HeroPowerSet> heroPowerSets = getHeroPowerSetRepository().findAllByPlayerCharacter(pc);
        List<HeroPower> heroPowers = getHeroPowerRepository().findAllByPlayerCharacter(pc);

        for (HeroPowerSet heroPowerSet : heroPowerSets) {
            add("----");
            add(new PairedComponent("POWER SET", heroPowerSet.getPowerSet().getName()));
            for (HeroPower heroPower : heroPowers) {
                if (heroPower.getHeroPowerSet().equals(heroPowerSet)) {
                    add(new PairedComponent("POWER", heroPower.getPower().getName()));
                }
            }
        }


    }

    private HeroPowerSetRepository getHeroPowerSetRepository() {
        return BeanFinder.getBean(HeroPowerSetRepository.class);
    }

    private HeroPowerRepository getHeroPowerRepository() {
        return BeanFinder.getBean(HeroPowerRepository.class);
    }


    public void updatePC() {
        //log.info("updating pc");
        pc = pcUpdater.updatePc(pc);
    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.PCUpdate.equals(entry.getType())) {
            if (pc != null && entry.getPcId().equals(pc.getId())) {
                BeanFinder.getBean(PlayerCharacterRepository.class).findById(pc.getId()).ifPresent(hero -> pc = hero);
                init();
            }
        }
    }


    public void removeTrait(Trait trait) {
        pc.getTraits().remove(trait);
        updatePC();
        init();
    }

    public void setPcUpdater(PCUpdater pcUpdater) {
        this.pcUpdater = pcUpdater;
    }

}
