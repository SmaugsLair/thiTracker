package com.smaugslair.thitracker.ui.games;

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

@CssImport(value = "./styles/color.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class GMCharacterView extends RegisteredVerticalLayout implements CharacterEditor {

    //private final static Logger log = LoggerFactory.getLogger(GMCharacterView.class);

    private final static int MAX_DRAMA = 10;

    private PCUpdater pcUpdater;
    //private SessionService sessionService;
    private PlayerCharacter pc = null;
    //private String color;


    public GMCharacterView() {
        setPadding(false);
        setSpacing(false);
        //log.info("Character view created");
    }

    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
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
        add(new PairedComponent(pc.getName(), user.getDisplayName()));
        //traitRows.add(new MetaRow(pc.getName(), user.getDisplayName(), color));
        add(new PairedComponent("aka", pc.getCivilianId()));
        //traitRows.add(new CivilianName(pc, null));
        //add(new PairedComponent("Prog points", pc.getProgressionTokens().toString()));
        ProgressionPoint pp = new ProgressionPoint(pc, this);
        add(new PairedComponent(pp.getLabel(), pp.getComponent()));
        //traitRows.add(new ProgressionPoint(pc, null));

        List<Trait> traits = pc.getTraits().stream()
                .filter(trait -> trait.getType().equals(TraitType.Hero))
                .sorted()
                .collect(Collectors.toList());
        add("Hero Traits");
        traits.forEach(trait -> {
            TraitField traitField = new TraitField(trait, this);
            add(new PairedComponent(traitField.getLabel(), traitField.getComponent()));
        });

        traits = pc.getTraits().stream()
                .filter(trait -> trait.getType().equals(TraitType.Drama))
                .sorted()
                .collect(Collectors.toList());
        add("Drama Traits");
        traits.forEach(trait ->{
            TraitField traitField = new TraitField(trait, this);
            add(new PairedComponent(traitField.getLabel(), traitField.getComponent()));
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
                BeanFinder.getBean(PlayerCharacterRepository.class).findById(pc.getId()).ifPresent(playerCharacter -> {
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


    private void calculateAbilityScores() {
        pc.getAbilityScores().forEach((ability, abilityScore) -> abilityScore.reset());
        List<HeroPowerSet> heroPowerSets = getHeroPowerSetRepository().findAllByPlayerCharacter(pc);
        heroPowerSets.forEach(heroPowerSet -> {
            heroPowerSet.getMods().forEach((ability, heroPowerSetMod) -> {
                pc.getAbilityScores().get(ability).adjustMods(heroPowerSetMod.getValue());
            });
        });
        List<HeroPower> heroPowers = getHeroPowerRepository().findAllByPlayerCharacter(pc);
        heroPowers.forEach(heroPower -> {
            heroPower.getMods().forEach((ability, heroPowerMod) -> {
                pc.getAbilityScores().get(ability).adjustMods(heroPowerMod.getValue());
            });
        });
    }

    public void setPcUpdater(PCUpdater pcUpdater) {
        this.pcUpdater = pcUpdater;
    }

}
