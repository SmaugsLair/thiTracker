package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public class GMActionBar extends HorizontalLayout {


    private final SessionService sessionService;

    private final GMSession gmSession;
    private Game game = null;

    public GMActionBar (SessionService sessionService, GMSession gmSession) {
        this.sessionService = sessionService;
        this.gmSession = gmSession;
        //setSpacing(false);
        setMargin(false);
        setPadding(false);
        setAlignItems(Alignment.CENTER);
        init();
    }

    public void init() {
        game = confirmData();
        if (game == null) {
            return;
        }
        add(new H2(game.getName()));
        add(createPCTimeLineButton());
        add(createInviteButton());
        add(createClearRollsButton());
        add(createClearActionsButton());
        add("Max Dice");
        add(createMaxDiceField());

    }


    private Game confirmData() {

        if (getGameId() == null) {
            add(new H1("No Game loaded"));
            return null;
        }

        final Game game = sessionService.getGameRepo().findById(getGameId()).orElse(new Game());

        if (game.getId() == null) {
            add(new H1("Game not found!"));
            return null;
        }
        User user = SecurityUtils.getLoggedInUser(sessionService);
        if (user == null || !game.getGameMasterId().equals(user.getId())) {
            add(new H1("You are not the GameMaster for this game!"));
            return null;
        }
        return game;

    }

    private Button createClearRollsButton() {
        return new UserSafeButton("Clear rolls", event -> {
            clearLogs(EventType.DiceRoll);
            gmSession.clearRolls();
            Entry entry = new Entry();
            entry.setGameId(getGameId());
            entry.setType(EventType.ClearRolls);
            Broadcaster.broadcast(entry);
        });
    }

    private Button createClearActionsButton() {
        return new UserSafeButton("Clear actions", event -> {
            clearLogs(EventType.GMAction);
            gmSession.clearActions();
        });
    }
    private Button createPCTimeLineButton() {
        return new UserSafeButton("PC Timeline", event -> {
            getUI().ifPresent(ui -> {
                ui.getPage().open("pcTimeLineView", "PC Timeline");
            });
        });
    }

    private Button createInviteButton() {

        TextField inviteName = new TextField();
        inviteName.setPlaceholder("name");
        TextField inviteEmail = new TextField();
        inviteEmail.setPlaceholder("email");

        ConfirmDialog inviteDialog = new ConfirmDialog(new VerticalLayout(inviteName, inviteEmail));
        Button inviteButton = new UserSafeButton("Invite player", event -> {
            sendPlayerInvitation(inviteName.getValue(), inviteEmail.getValue());
            inviteDialog.close();
        });
        inviteDialog.setConfirmButton(inviteButton);
        return new UserSafeButton("Invite", event -> inviteDialog.open());
    }

    private IntegerField createMaxDiceField() {

        IntegerField maxDice = new IntegerField();
        maxDice.setWidth("120px");
        maxDice.setValue(game.getMaxDice());
        maxDice.setStepButtonsVisible(true);
        maxDice.setMin(1);
        maxDice.addValueChangeListener(event -> {
            game.setMaxDice(event.getValue());
            sessionService.getGameRepo().save(game);
            Entry entry = new Entry(EventType.MaxDiceUpdate);
            entry.setGameId(getGameId());
            Broadcaster.broadcast(entry);

        });
        return maxDice;
    }

    private void clearLogs(EventType type) {
        //NameValue nameValue = new NameValue("gameId", getGameId());
        List<Entry> list = sessionService.getEntryRepo().findByGameId(getGameId());
        for (Entry entry : list) {
            if (entry.getType().equals(type)) {
                sessionService.getEntryRepo().delete(entry);
            }
        }
    }

    private void sendPlayerInvitation(String name, String email) {

        User gm = SecurityUtils.getLoggedInUser(sessionService);
        if (gm == null) {
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setCc(gm.getEmail());

        msg.setSubject("Invitation to The Hero Instant App");

        String sb = "Hello " + name + "\n You've been invited to create an account on The Hero Instance app and join a game with " +
                gm.getDisplayName() + "!\n" + "To start, point your browser to " +
                sessionService.getAppUrl() + ", create an account, sign in, click on the Friends tab, " +
                "and enter \nPlayer name: " + gm.getDisplayName() + "\nFriend code: " +
                gm.getFriendCode() + "\nin the Friend Finder. Then create a character and " +
                gm.getDisplayName() + " will be able to add you to their game.";
        msg.setText(sb);

        sessionService.getJavaMailSender().send(msg);
    }

    public Long getGameId() {
        return sessionService.getGameId();
    }


}
