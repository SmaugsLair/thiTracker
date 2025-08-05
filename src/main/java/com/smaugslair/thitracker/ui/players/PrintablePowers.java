package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.pc.HeroPower;
import com.smaugslair.thitracker.data.pc.HeroPowerSet;
import com.smaugslair.thitracker.data.pc.HeroSubPower;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.templates.Template;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.util.AbilityModsRenderer;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@PermitAll
@Route("printablePowers")
public class PrintablePowers extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(PrintablePowers.class);

    //private final SessionService sessionService;
    //private final IFrame iFrame = new IFrame();

    public PrintablePowers(SessionService sessionService) {
        //log.info("Constructor");
        //this.sessionService = sessionService;
        Optional<Template> template = sessionService.getTemplateRepository().findByName("powerPrintTemplate");
        if (template.isPresent()) {
            final Map<String, Object> root = new HashMap<>();
            User user = sessionService.getUser();
            root.put("user", user.getDisplayName());
            PlayerCharacter pc = sessionService.getPc();
            root.put("pc", pc);

            ArrayList<PrintableHeroPowerSet> powerSets = new ArrayList<>();

            List<HeroPowerSet> heroPowerSets = sessionService.getHpsRepo().findAllByPlayerCharacter(pc);
            List<HeroPower> heroPowers = sessionService.getHpRepo().findAllByPlayerCharacter(pc);

            for (HeroPowerSet heroPowerSet : heroPowerSets) {
                PrintableHeroPowerSet copy = new PrintableHeroPowerSet(heroPowerSet);
                for (HeroPower heroPower : heroPowers) {
                    if (heroPower.getHeroPowerSet().equals(heroPowerSet)) {
                        copy.addPower(heroPower);
                    }
                }
                powerSets.add(copy);
            }
            root.put("powerSets", powerSets);


            String text = sessionService.getFreemarkerService().applyTemplate(template.get().getText(), root);
            add(new Html(text));
        }
        else {
            add(new Html("<h1>No template named 'powerPrintTemplate' found.</h1>"));
        }


    }

    public class PrintableHeroPowerSet {

        private final HeroPowerSet powerSet;
        private ArrayList<PrintableHeroPower> powers = new ArrayList<>();

        public PrintableHeroPowerSet(HeroPowerSet powerSet) {
            this.powerSet = powerSet;
            /*setName(powerSet.getName());
            setOpenText(powerSet.getOpenText());
            setAbilityText(powerSet.getAbilityText());
            setPowersText(powerSet.getPowersText());
            setPowerSetMods(powerSet.getPowerSetMods());*/
        }

        public String getName() {
            return powerSet.getPowerSet().getName();
        }

        public String getOpenText() {
            return powerSet.getPowerSet().getOpenText();
        }

        public String getAbilityText() {
            return powerSet.getPowerSet().getAbilityText();
        }
        public String getPowersText() {
            return powerSet.getPowerSet().getPowersText();
        }

        public String getPowerSetMods() {
            return AbilityModsRenderer.renderAmString(powerSet.getMods().values());
        }

        public ArrayList<PrintableHeroPower> getPowers() {
            return powers;
        }

        public void addPower(HeroPower power) {
            powers.add(new PrintableHeroPower(power));
        }

    }

    public class PrintableHeroPower {
        private final HeroPower power;

        public PrintableHeroPower(HeroPower power) {
            this.power = power;
        }

        public String getName() {
            return power.getName();
        }

        public String getShort() {
            return power.getShortDescr();
        }

        public String getFull() {
            return power.getPower().getFullDescr();
        }

        public String getExtra() {
            if (!power.getMods().isEmpty()) {
                return power.getModText();
            }
            return "";
        }

        public Set<HeroSubPower> getSubPowers() {
            return power.getSubPowers();
        }

    }

}
