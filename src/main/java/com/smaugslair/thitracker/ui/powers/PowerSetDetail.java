package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.services.PowersCache;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;
import java.util.SortedSet;

@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class PowerSetDetail extends VerticalLayout {

    public PowerSetDetail(PowerSet powerSet, PowersCache powersCache) {
        setId(powerSet.getName());

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );
        formLayout.addFormItem(new Paragraph(powerSet.getOpenText()), powerSet.getName());
        formLayout.addFormItem(new Paragraph(powerSet.getAbilityText()), "Abilities");
        formLayout.addFormItem(new Paragraph(powerSet.getAbilityModsText()), "");
        formLayout.addFormItem(new Details("", new Paragraph(powerSet.getPowersText())), "Details");

        VerticalLayout powersLayout = new VerticalLayout();
        powersLayout.setWidthFull();

        Map<Integer, SortedSet<Power>> powerMap = powersCache.getPowersMap().get(powerSet.getName());

        powerMap.forEach((tier, powers) -> {
            PowersGrid grid = new PowersGrid(powers);
            Details details = new Details("Tier " + tier.toString() + " - "+powers.size()+ " powers", grid);
            powersLayout.add(details);
        });

        add(formLayout, powersLayout);


    }

}
