package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.HeroPower;
import com.smaugslair.thitracker.data.pc.HeroPowerSet;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class PowerSetLayout extends VerticalLayout {
    public PowerSetLayout(HeroPowerSet powerSet, Collection<HeroPower> powers, CharacterSheet characterSheet) {

        setSpacing(false);

        SortedSet<HeroPower> set = new TreeSet<>();
        for (HeroPower heroPower : powers) {
            if (heroPower.getHeroPowerSet().equals(powerSet)) {
                set.add(heroPower);
            }
        }

        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.setJustifyContentMode(JustifyContentMode.BETWEEN);
        top.add(new Span(powerSet.getPowerSet().getName()));
        top.add(new Span(String.valueOf(set.size())));
        add(top);
        for (HeroPower heroPower : set) {
            UnorderedList content = new UnorderedList();
            //content.add(new ListItem(heroPower.getPower().getShortDescr()));
            String title = heroPower.getPower().getName();
            /*if (heroPower.getPower().getPowerSets() == null) {
                title = "!!-"+title+"-!!";
                content.add(new ListItem("!! This power has been orphaned. Consult with your GM, remove this power and choose a replacement!!"));
            }*/
            content.add(new ListItem(heroPower.getPower().getFullDescr()));
            if (!heroPower.getPower().getAbilityMods().isEmpty()) {
                content.add(new ListItem(heroPower.getPower().getAbilityMods()));
            }
            Details details = new Details(title, content);
            //details.
            details.setOpened(false);
            //details.addThemeVariants(DetailsVariant.REVERSE);
            add(details);
        }
        if (characterSheet != null) {
            UserSafeButton button =
                    new UserSafeButton("Choose new " + powerSet.getPowerSet().getName() +" power",
                            event -> {
                                characterSheet.showPowerChoiceDialog(powerSet);

                            });
            add(button);
        }


    }
}
