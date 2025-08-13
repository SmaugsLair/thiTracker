package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.util.BeanFinder;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@UIScope
public class GMActionBar extends HorizontalLayout {

    //private static final Logger log = LoggerFactory.getLogger(GMActionBar.class);

    private final GMSession gmSession;

    private final Game game;

    public GMActionBar (GMSession gmSession, Game game) {
        this.gmSession = gmSession;
        this.game = game;

        //log.info("Created");
        setMargin(false);
        setPadding(false);
        setAlignItems(Alignment.CENTER);
        //log.info("init");
        add(new H2(game.getName()));
        add(createPCTimeLineButton());
        add(createInviteButton());
        add(createClearRollsButton());
        add(createClearActionsButton());
        add("Max Dice");
        add(createMaxDiceField());

    }

    private Button createClearRollsButton() {
        return new UserSafeButton("Clear rolls", event -> {
            clearLogs(EventType.DiceRoll);
            gmSession.clearRolls();
            Entry entry = new Entry();
            entry.setGameId(game.getId());
            entry.setType(EventType.ClearRolls);
            Broadcaster.broadcast(entry);
        });
    }

    private Button createClearActionsButton() {
        return new UserSafeButton("Clear actions", event -> {
            clearLogs(EventType.GMAction);
            gmSession.clearActions();
            //gmSession.clearActions();
        });
    }
    private Button createPCTimeLineButton() {
        return new UserSafeButton("PC Timeline", event -> {
            getUI().ifPresent(ui -> {
                ui.getPage().open("/pcTimeLineView/"+game.getId(), "PC Timeline");
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
            BeanFinder.getBean(GameRepository.class).save(game);
            Entry entry = new Entry(EventType.MaxDiceUpdate);
            entry.setGameId(game.getId());
            Broadcaster.broadcast(entry);

        });
        return maxDice;
    }

    private void clearLogs(EventType type) {
        EntryRepository entryRepository = BeanFinder.getBean(EntryRepository.class);
        List<Entry> list = entryRepository.findByGameId(game.getId());
        for (Entry entry : list) {
            if (entry.getType().equals(type)) {
                entryRepository.delete(entry);
            }
        }
    }

    private void sendPlayerInvitation(String name, String email) {

        SessionService sessionService = BeanFinder.getBean(SessionService.class);

        User gm = sessionService.getLoggedInUser();
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

        BeanFinder.getBean(JavaMailSender.class).send(msg);
    }
}
