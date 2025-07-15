package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.pc.HeroPower;
import com.smaugslair.thitracker.data.pc.HeroPowerSet;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.templates.Template;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@PermitAll
@Route("printableSheet")
public class PrintableSheet extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(PrintableSheet.class);

    //private final SessionService sessionService;
    //private final IFrame iFrame = new IFrame();

    public PrintableSheet(SessionService sessionService) {
        log.info("Constructor");
        //this.sessionService = sessionService;
        Optional<Template> template = sessionService.getTemplateRepository().findByName("charSheetTemplate");
        if (template.isPresent()) {
            final Map<String, Object> root = new HashMap<>();
            User user = sessionService.getUser();
            root.put("user", user.getDisplayName());
            PlayerCharacter pc = sessionService.getPc();
            root.put("pc", pc);

            pc.getAbilityScores().forEach((ability, abilityScore) -> {
                root.put(ability.getDisplayName(), abilityScore.getPoints());
            });

            Set<String> heroTraits = new HashSet<>();
            Set<String> dramaTraits = new HashSet<>();

            for (Trait trait : pc.getTraits()) {
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

            root.put("heroPowerSet0", "TBD");
            root.put("heroPowerList0", new ArrayList<>());
            root.put("totalPower0", "");
            root.put("heroPowerSet1", "TBD");
            root.put("heroPowerList1", new ArrayList<>());
            root.put("totalPower1", "");


            List<HeroPowerSet> heroPowerSets = sessionService.getHpsRepo().findAllByPlayerCharacter(pc);
            List<HeroPower> heroPowers = sessionService.getHpRepo().findAllByPlayerCharacter(pc);

            int totalPowers = 0;
            for (int psi = 0; psi <=1; ++psi) {
                if (psi >= heroPowerSets.size()) {
                    break;
                }
                HeroPowerSet heroPowerSet = heroPowerSets.get(psi);
                root.put("heroPowerSet"+psi, heroPowerSet.getPowerSet().getName());
                List<String> list = new ArrayList<>();
                root.put("heroPowerList"+psi, list);
                int hpCount = 0;
                for (HeroPower heroPower : heroPowers) {
                    if (heroPower.getHeroPowerSet().equals(heroPowerSet)) {
                        list.add(heroPower.getName());
                        ++hpCount;
                        ++totalPowers;
                    }
                }
                root.put("totalPower"+psi, hpCount);
            }
            root.put("totalPowers", totalPowers);

            log.info(root.keySet().toString());

            String text = sessionService.getFreemarkerService().applyTemplate(template.get().getText(), root);
            add(new Html(text));
        }
        else {
            add(new Html("<h1>No template named 'charSheetTemplate' found.</h1>"));
        }


    }

}
