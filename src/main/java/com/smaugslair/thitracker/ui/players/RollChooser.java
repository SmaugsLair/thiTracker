package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.pc.AbilityScore;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.rules.Ability;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RollChooser extends HorizontalLayout {

    private final SessionService sessionService;
    private final AbilityChoice abilityChoice;


    public RollChooser(SessionService sessionService, AbilityChoice abilityChoice) {
        this.sessionService = sessionService;
        this.abilityChoice = abilityChoice;
        PlayerCharacter pc = sessionService.getPc();
        setAlignItems(Alignment.CENTER);

        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setAlignItems(Alignment.CENTER);
        add(leftLayout);
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setAlignItems(Alignment.CENTER);
        add(rightLayout);

        for (int i = 0; i < 5; ++i) {
            leftLayout.add(createRollLauncher(pc.getAbilityScores().get(Ability.getAt(i, 0))));
            rightLayout.add(createRollLauncher(pc.getAbilityScores().get(Ability.getAt(i, 1))));

        }
    }

    private UserSafeButton createRollLauncher(AbilityScore abilityScore) {
        UserSafeButton button = new UserSafeButton(abilityScore.getAbility().getDisplayName()+" "+abilityScore.getPoints(), buttonClickEvent -> {
            abilityChoice.setChoice(abilityScore);
        });
        button.setWidthFull();
        return button;
    }

}
