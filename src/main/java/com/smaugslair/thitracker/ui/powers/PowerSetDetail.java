package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.data.templates.Template;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.FormattedTextBlock;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;

@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
@JsModule("./src/copytoclipboard.js")
public class PowerSetDetail extends VerticalLayout {


    private Map<String, Object> root = new HashMap<>();

    private final TextArea textArea = new TextArea();
    private final Dialog dialog = new Dialog();

    public PowerSetDetail(SessionService sessionService, PowerSet powerSet, PowersCache powersCache) {
        textArea.setWidthFull();
        Button button = new Button("Copy to clipboard", VaadinIcon.COPY.create());
        button.addClickListener(
                e -> UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", textArea.getValue())
        );
        dialog.add(button);
        dialog.add(textArea);
        dialog.setWidth("800px");


        add(new UserSafeButton("Text version", buttonClickEvent -> {
            Optional<Template> template = sessionService.getTemplateRepository().findByName("powerSetTemplate");
            if (template.isPresent()) {
                String text = sessionService.getFreemarkerService().applyTemplate(template.get().getText(), root);
                textArea.setValue(text);
            }
            else {
                textArea.setValue("Failed to find template named 'powerSetTemplate'");
            }
            dialog.open();
        }));
        setId(powerSet.getName());

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );
        formLayout.addFormItem(new FormattedTextBlock(powerSet.getOpenText(), powerSet.getName()), "");
        formLayout.addFormItem(new FormattedTextBlock(powerSet.getAbilityText(), "Abilities"), "");
        formLayout.addFormItem(new FormattedTextBlock(powerSet.getAbilityModsText(), "AbilityMods"), "");
        formLayout.addFormItem(new Details("", new FormattedTextBlock(powerSet.getPowersText(), "")), "Details");

        VerticalLayout powersLayout = new VerticalLayout();
        powersLayout.setWidthFull();

        Map<Integer, SortedSet<Power>> powerMap = powersCache.getPowersMap().get(powerSet.getName());

        powerMap.forEach((tier, powers) -> {
            PowersGrid grid = new PowersGrid(powers);
            Details details = new Details("Tier " + tier.toString() + " - "+powers.size()+ " powers", grid);
            powersLayout.add(details);
        });

        add(formLayout, powersLayout);

        root.put("powerSet", powerSet);
        root.put("powerMap", powerMap);


    }

}
