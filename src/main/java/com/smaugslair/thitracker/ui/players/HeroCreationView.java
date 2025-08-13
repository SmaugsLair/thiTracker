package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.HeroView;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.*;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Hero Creation")
@Route(value = "herocreation", layout = MainView.class)
@UIScope
public class HeroCreationView extends VerticalLayout {


    public HeroCreationView(TitleBar titleBar, SessionService sessionService, UIService uiService, PlayerCharacterRepository playerCharacterRepository) {

        titleBar.setTitle("Hero Creation");

        ValidTextField pcName = new ValidTextField();
        pcName.addValidator(new StringLengthValidator("2 char min", 2, 40));
        pcName.setPlaceholder("Character name");

        ConfirmDialog confirmDialog = new ConfirmDialog(pcName);

        Button saveButton = new UserSafeButton("Save", event -> {
            if (pcName.isValid()) {
                PlayerCharacter pc = new PlayerCharacter();
                pc.setName(pcName.getValue());
                pc.setCivilianId(pcName.getValue());
                pc.setUserId(sessionService.getLoggedInUser().getId());
                for (int i = 1; i < 4; ++i) {
                    for (TraitType type : TraitType.values()) {
                        Trait trait = new Trait();
                        trait.setSortOrder(type == TraitType.Hero ? i : i + 3);
                        trait.setType(type);
                        trait.setName(type.name() + " trait " + i);
                        trait.setPoints(0);
                        trait.setPlayerCharacter(pc);
                        pc.getTraits().add(trait);
                    }
                }
                pc = playerCharacterRepository.save(pc);
                confirmDialog.close();
                uiService.fireEvent(new AppEvent(AppEvent.AppEventType.MenuChange, pc.getId()));
                UI.getCurrent().navigate(HeroView.class, pc.getId());
                //init();
            }

        });
        confirmDialog.setConfirmButton(saveButton);
        confirmDialog.open();

        add(new UserSafeButton("Create new Hero", e -> confirmDialog.open() ));


    }

}
