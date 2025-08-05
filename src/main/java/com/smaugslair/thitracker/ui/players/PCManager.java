package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.WelcomeView;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.components.events.HeroCountEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PCManager extends VerticalLayout {

    private final Logger log = LoggerFactory.getLogger(PCManager.class);

    private final SessionService sessionService;
    private final PlayerCharacter hero;
    //private final Consumer<PlayerCharacter> pcSelector;

    public PCManager(SessionService sessionService, PlayerCharacter playerCharacter) {
        this.sessionService = sessionService;
        this.hero = playerCharacter;

        addListener(HeroCountEvent.class, sessionService.getHeroCountListener()::heroCountChanged);

        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        User user = SecurityUtils.getLoggedInUser(sessionService);
        if (user == null || hero == null) return;
        Game game = null;
        if (hero.getGameId() != null) {
            game = sessionService.getGameRepo().findById(hero.getGameId()).orElse(null);
        }

        ConfirmDialog deleteDialog = new ConfirmDialog("Are you sure you want to delete the character "+hero.getName()+"?");
        Button confirmButton = new UserSafeButton("Delete", event -> {
            List<TimeLineItem> items = sessionService.getTliRepo().findByPcId(hero.getId());
            items.forEach(item -> {
                item.setPcId(null);
                item.setName(hero.getName());
            });
            sessionService.getTliRepo().saveAll(items);
            sessionService.getHpsRepo().deleteAll(sessionService.getHpsRepo().findAllByPlayerCharacter(hero));
            sessionService.getPcRepo().delete(hero);
            getEventBus().fireEvent(new HeroCountEvent(this, false));
            deleteDialog.close();
            getUI().ifPresent(ui -> ui.navigate(WelcomeView.class));
            sessionService.getTitleBar().setTitle("");
            //refresh();
        });
        deleteDialog.setConfirmButton(confirmButton);

        //VerticalLayout layout = new VerticalLayout();
        if (hero.getGameId() != null) {
            Button launch = new UserSafeButton("Launch "+ game.getName());
            launch.addClickListener(e -> launch.getUI().ifPresent(ui -> {
                sessionService.setGameId(hero.getGameId());
                sessionService.setPc(hero);
                ui.navigate("playersession");
            }));
            add(launch);
        }
        else {
            Icon icon = VaadinIcon.FROWN_O.create();
            icon.setSize("16px");
            icon.setTooltipText("Character has no game.");
            add(icon);
        }

        //Button sheet = new UserSafeButton("Edit sheet -->", event -> pcSelector.accept(pc));
       /* Button sheet = new UserSafeButton("Edit sheet -->");
        layout.add(sheet);*/


        Button printableSheet = new UserSafeButton("Printable Sheet", event -> {
            sessionService.setPc(hero);
            getUI().ifPresent(ui -> {
                ui.getPage().open("printableSheet", "Printable Character Sheet");
            });
        });
        printableSheet.setTooltipText("Opens in new window");
        add(printableSheet);

        Button printablePowers = new UserSafeButton("Printable Powers", event -> {
            sessionService.setPc(hero);
            getUI().ifPresent(ui -> {
                ui.getPage().open("printablePowers", "Printable Character Powers");
            });
        });
        printablePowers.setTooltipText("Opens in new window");
        add(printablePowers);


        Button delete = new UserSafeButton("Delete", event -> deleteDialog.open());
        add(delete);

    }

}
