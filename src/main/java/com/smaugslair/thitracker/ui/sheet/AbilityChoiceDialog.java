package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.Moddable;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.ui.components.MultiColumnLayout;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AbilityChoiceDialog extends Dialog {

    private final int choices;
    List<AbilityChoiceComponent> components = new ArrayList<>(10);

    public <T extends Moddable> AbilityChoiceDialog(T heroPowerSet, Consumer<T> consumer, int choices) {
        this.choices = choices;
        setModal(true);

        add(new Label("Choose "+choices+" abilities to improve:"));

        MultiColumnLayout multiColumnLayout = new MultiColumnLayout(2);
        multiColumnLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        add(multiColumnLayout);
        for (int i = 0; i < 5; ++i) {
            AbilityChoiceComponent acc = new AbilityChoiceComponent(Ability.getAt(i, 0));
            components.add(acc);
            multiColumnLayout.addToNextColumn(acc);
            acc = new AbilityChoiceComponent(Ability.getAt(i, 1));
            components.add(acc);
            multiColumnLayout.addToNextColumn(acc);
        }
        add(new UserSafeButton("Save", event -> {
            if (valid()) {
                List<Ability> abilities = new ArrayList<>();
                components.forEach(component -> {
                    if (component.getValue() > 0) {
                        heroPowerSet.addMod(component.getAbility(), component.getValue());
                    }
                });
                consumer.accept(heroPowerSet);
                close();
            }
            else {
                Notification.show("Must choose a total of " + choices, 5000, Notification.Position.MIDDLE);
            }
        }));

    }

    private boolean valid() {
        int sum = components.stream().mapToInt(AbilityChoiceComponent::getValue).sum();
        return sum == choices;
    }
}
