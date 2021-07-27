package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.services.PowersCache;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;

import java.util.List;
import java.util.Map;

public class PowerSetDetail extends VerticalLayout {

    public PowerSetDetail(PowerSet powerSet, PowersCache powersCache) {
        setId(powerSet.getName());

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );
        formLayout.addFormItem(new Paragraph(powerSet.getOpenText()), powerSet.getName());
        formLayout.addFormItem(new Paragraph(powerSet.getAbilityText()), "Abilities");
        formLayout.addFormItem(new Paragraph(powerSet.getAbilityMods()), "");
        formLayout.addFormItem(new Details("", new Paragraph(powerSet.getPowersText())), "Details");

        //Accordion powersAccordion = new Accordion();
        //powersAccordion.close();

        VerticalLayout powersLayout = new VerticalLayout();
        powersLayout.setWidthFull();

        Map<Integer, List<Power>> powerMap = powersCache.getPowersMap().get(powerSet.getName());

        powerMap.forEach((tier, powers) -> {
            Grid<Power> grid = new Grid<>();
            grid.setItems(powers);


            grid.addColumn(new NativeButtonRenderer<>(
                    item -> grid.isDetailsVisible(item) ? "-" : "+",
                    item -> grid.setDetailsVisible(item, !grid.isDetailsVisible(item))));
            grid.addColumn(Power::getName).setHeader("Name");
            grid.addColumn(Power::getShortDescr).setHeader("Short Description");
            grid.addColumn(Power::getPrereq).setHeader("Prerequisites");
            grid.addColumn(Power::getMaxTaken).setHeader("Limit");

            grid.setItemDetailsRenderer(new ComponentRenderer<>(power -> {
                return new Paragraph(power.getFullDescr());
            }));

            grid.getColumns().forEach(powerColumn -> powerColumn.setAutoWidth(true));
            grid.setWidthFull();
            grid.setHeightByRows(true);

            Details details = new Details("Tier " + tier.toString() + " - "+powers.size()+ " powers", grid);
            powersLayout.add(details);
        });

        add(formLayout, powersLayout);


    }

}
