package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.pc.*;
import com.smaugslair.thitracker.data.templates.Template;
import com.smaugslair.thitracker.data.templates.TemplateRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.services.FreemarkerService;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.tuple.MutableTriple;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@PermitAll
@Route("printableSheet")
public class PrintableSheet extends AbstractHeroView {

    //private static final Logger log = LoggerFactory.getLogger(PrintableSheet.class);

    private final TemplateRepository templateRepository;
    private final SessionService sessionService;
    private final HeroPowerSetRepository heroPowerSetRepository;
    private final HeroPowerRepository heroPowerRepository;
    private final FreemarkerService freemarkerService;

    protected PrintableSheet(UIService uiService, GameRepository gameRepository, PlayerCharacterRepository playerCharacterRepository, TitleBar titleBar, TemplateRepository templateRepository, UserRepository userRepository, SessionService sessionService, HeroPowerSetRepository heroPowerSetRepository, HeroPowerRepository heroPowerRepository, FreemarkerService freemarkerService) {
        super(uiService, gameRepository, playerCharacterRepository, titleBar);
        this.templateRepository = templateRepository;
        this.sessionService = sessionService;
        this.heroPowerSetRepository = heroPowerSetRepository;
        this.heroPowerRepository = heroPowerRepository;
        this.freemarkerService = freemarkerService;
    }


    protected void init() {
        Optional<Template> template = templateRepository.findByName("charSheetTemplate");
        if (template.isPresent()) {
            final Map<String, Object> root = new HashMap<>();
            User user = sessionService.getUser();
            root.put("user", user.getDisplayName());
            root.put("pc", hero);

            hero.getAbilityScores().forEach((ability, abilityScore) -> {
                root.put(ability.toString(), abilityScore.getPoints());
                //log.info("added: " +ability);
            });

            Set<String> heroTraits = new HashSet<>();
            Set<String> dramaTraits = new HashSet<>();

            for (Trait trait : hero.getTraits()) {
                switch (trait.getType()) {
                    case Hero:
                        heroTraits.add(trait.getName());
                        break;
                    case Drama:
                        dramaTraits.add(trait.getName());
                        break;
                }
            }

            root.put("heroTraits", heroTraits);
            root.put("dramaTraits", dramaTraits);

            root.put("heroPowerSet0", "");
            //root.put("heroPowerList0", new ArrayList<>());
            root.put("totalPower0", "");
            root.put("heroPowerSet1", "");
            //root.put("heroPowerList1", new ArrayList<>());
            root.put("totalPower1", "");



            List<HeroPowerSet> heroPowerSets = heroPowerSetRepository.findAllByPlayerCharacter(hero);
            List<HeroPower> heroPowers = heroPowerRepository.findAllByPlayerCharacter(hero).stream()
                    .sorted().collect(Collectors.toList());

            int maxPowerRow = Math.max(heroPowers.size()+1, 11);

            ArrayList<MutableTriple<Integer, String, String>> powers = new ArrayList<>();
            for (int i = 1; i < maxPowerRow; i++) {
                powers.add(new MutableTriple<>(i, "", ""));
            }
            root.put("powers", powers);
            int totalPowers = 0;
            for (int psi = 0; psi <=1; ++psi) {
                if (psi >= heroPowerSets.size()) {
                    break;
                }
                HeroPowerSet heroPowerSet = heroPowerSets.get(psi);
                root.put("heroPowerSet"+psi, heroPowerSet.getPowerSet().getName());
                //List<String> list = new ArrayList<>();
                //root.put("heroPowerList"+psi, list);
                int hpCount = 0;
                for (HeroPower heroPower : heroPowers) {
                    if (heroPower.getHeroPowerSet().equals(heroPowerSet)) {

                        String more = "";

                        if (!heroPower.getMods().isEmpty()) {
                            more = ": " + heroPower.getModText();
                        }
                        if (!heroPower.getSubPowers().isEmpty()) {
                            more += ": ";
                            for (HeroSubPower subPower : heroPower.getSubPowers()) {
                                more += subPower.getName() +", ";
                            }
                            more = more.substring(0, more.length() - 2);
                        }
                        if (psi == 0) {
                            powers.get(hpCount).setMiddle(heroPower.getName()+ more);
                        }
                        else {
                            powers.get(hpCount).setRight(heroPower.getName()+ more);
                        }
                        ++hpCount;
                        ++totalPowers;
                    }
                }
                root.put("totalPower"+psi, hpCount);
            }
            root.put("totalPowers", totalPowers);

            //log.info(root.keySet().toString());

            String text = freemarkerService.applyTemplate(template.get().getText(), root);
            add(new Html(text));
        }
        else {
            add(new Html("<h1>No template named 'charSheetTemplate' found.</h1>"));
        }


    }

}
