package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.services.CacheService;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DiceRoller extends RegisteredVerticalLayout {

    private final String exp_val = "Exp val: ";

    private enum TokenType {HERO, DRAMA}

    private static final Logger log = LoggerFactory.getLogger(DiceRoller.class);

    private final SessionService sessionService;
    private final CacheService cacheService;

    private IntegerField d10s = new IntegerField();
    private IntegerField maxDice = new IntegerField();
    private FormLayout resultLayout = new FormLayout();
    private Label progressLabel = new Label();
    private ProgressBar progressBar = new ProgressBar();
    private Label expectedValue = new Label(exp_val+(3*5.5));
    private Label expectedHeroValue = new Label(exp_val+(4*5.5));
    private TextField diceText = new TextField();
    private TextField droppedText = new TextField();
    private Label droppedLabel = new Label("Dropped");

    private Button preHeroRollButton = new Button("Hero roll 4");

    Button postHeroRollButton = new Button("Roll 1 more, drop lowest",
            event -> rollAgain(TokenType.HERO));

    private List<Integer> dice = new ArrayList<>();
    private int sum = 0;


    public DiceRoller(SessionService sessionService, CacheService cacheService) {
        this.sessionService = sessionService;
        this.cacheService = cacheService;


        //setPadding(false);
        setMargin(false);
        setSpacing(false);

        PlayerCharacter pc = sessionService.getPc();


        FormLayout rollLayout = new FormLayout();
        TextField heroField = new TextField();
        heroField.setLabel("Hero");
        heroField.setValue(pc.getName());
        heroField.setReadOnly(true);
        heroField.setWidth("150px");


        //rollLayout.addFormItem(new Span("Hero: "+pc.getName()), "Dice Roller");
        d10s.setLabel("d10s to roll");
        d10s.setWidth("100px");
        d10s.setValue(3);
        d10s.setHasControls(true);
        d10s.setMin(1);

        maxDice.setLabel("Max dice");
        maxDice.setWidth("60px");
        maxDice.setValue(getMaxDiceForGame());
        maxDice.setReadOnly(true);

        HorizontalLayout topLine = new HorizontalLayout(heroField, d10s, maxDice);
        topLine.setMargin(false);
        topLine.setPadding(false);

        rollLayout.add(topLine);

        Button rollButton = new Button("Roll 3");
        preHeroRollButton.setEnabled(pc.getHeroPoints() > 0);


        VerticalLayout normal = new VerticalLayout();
        normal.setWidthFull();
        normal.setAlignItems(Alignment.CENTER);
        normal.setPadding(false);
        normal.setMargin(false);
        normal.setSpacing(false);
        VerticalLayout hero = new VerticalLayout();
        hero.setWidthFull();
        hero.setAlignItems(Alignment.CENTER);
        hero.setPadding(false);
        hero.setMargin(false);
        hero.setSpacing(false);

        normal.add(rollButton, expectedValue);
        hero.add(preHeroRollButton, expectedHeroValue);

        rollLayout.addFormItem(hero, normal);



        d10s.addValueChangeListener(event -> {
            rollButton.setText("Roll "+d10s.getValue());
            expectedValue.setText(exp_val+calcExpected(d10s.getValue()));

            preHeroRollButton.setText("Hero roll "+(d10s.getValue()+1));
            expectedHeroValue.setText(exp_val+calcExpected(d10s.getValue()+1));
        });
        maxDice.addValueChangeListener(event -> {
            expectedValue.setText(exp_val+calcExpected(d10s.getValue()));
            expectedHeroValue.setText(exp_val+calcExpected(d10s.getValue()+1));
        });

        rollButton.addClickListener(event -> initialRoll(false));
        preHeroRollButton.addClickListener(event -> initialRoll(true));

        add(rollLayout);

        resultLayout.setVisible(false);
        resultLayout.addFormItem(progressBar, progressLabel);
        diceText.setSizeFull();
        diceText.setReadOnly(true);
        resultLayout.addFormItem(diceText,"Dice");
        droppedText.setSizeFull();
        droppedText.setReadOnly(true);
        resultLayout.addFormItem(droppedText, droppedLabel);

        Button dramaRoll = new Button("Roll 2 more, drop lowest",
                event -> rollAgain(TokenType.DRAMA));
        resultLayout.addFormItem(postHeroRollButton, "Spend Hero Token");
        postHeroRollButton.setEnabled(pc.getHeroPoints() > 0);
        resultLayout.addFormItem(dramaRoll, "Spend Drama Token");

        add(resultLayout);
    }

    private int captureD10roll() {
        int roll = (int)(Math.random() * 10) + 1;
        //log.info("rolled:"+roll);
        sum += roll;
        dice.add(roll);
        return roll;
    }

    //Assumes sorted
    private int dropLowest() {
        int least = dice.remove(0);
        sum -= least;
        return least;
    }

    private double calcPercent(double sum, int rolled, int max) {
        int divisor = Math.min(rolled, max);
        return Math.min(sum/(divisor*10), 1.0);
    }

    private String calcExpected(int dice) {
        //int dice = d10s.getValue();
        int max = maxDice.getValue();
        if (dice > max) {
            return " > " + (max *5.5);
        }
        return String.valueOf(dice * 5.5);
    }

    private void initialRoll(boolean hero) {
        dice.clear();
        sum = 0;
        int total = d10s.getValue() + (hero ? 1 : 0);
        for (int count = 0; count < total; ++count) {
            captureD10roll();
        }
        Collections.sort(dice);
        List<Integer> dropped = new ArrayList<>();
        while (dice.size() > maxDice.getValue()) {
            dropped.add(dropLowest());
        }
        prepareResults(dropped, hero?"HERO":null);

        if (hero) {
            spendToken(TokenType.HERO);
        }

        resultLayout.setVisible(true);

    }

    private void prepareResults(List<Integer> dropped, String rollType) {

        double percent = calcPercent(sum, d10s.getValue(), maxDice.getValue());
        progressBar.setValue(percent);
        ProgressBarVariant variant = ProgressBarVariant.LUMO_SUCCESS;
        if (percent < .55) {
            variant = ProgressBarVariant.LUMO_ERROR;
        }
        progressBar.removeThemeVariants(ProgressBarVariant.values());
        progressBar.addThemeVariants(variant);
        progressLabel.setText("Total: "+sum);
        diceText.setValue(dice.toString());
        droppedLabel.setVisible(!dropped.isEmpty());
        droppedText.setVisible(!dropped.isEmpty());
        droppedText.setValue(dropped.toString());

        StringBuffer sb = new StringBuffer();
        sb.append(sessionService.getPc().getName()).append(" rolled ").append(sum);
        sb.append("  ").append(dice);
        if (rollType != null) {
            sb.append(" ").append(rollType);
        }


        Entry message = new Entry();
        message.setGameId(sessionService.getGameId());
        message.setType(EventType.DiceRoll);
        message.setText(sb.toString());

        cacheService.getEntryCache().save(message);

        Broadcaster.broadcast(message);

    }

    private void spendToken(TokenType tokenType) {

        PlayerCharacter pc = sessionService.getPc();
        if (tokenType.equals(TokenType.HERO)) {
            pc.setHeroPoints(pc.getHeroPoints()-1);
        }
        else {
            pc.setDramaPoints(pc.getDramaPoints()+1);
        }
        cacheService.getPcCache().save(pc);
        Entry entry = new Entry();
        entry.setPcId(pc.getId());
        entry.setGameId(sessionService.getGameId());
        entry.setType(EventType.PCUpdate);
        Broadcaster.broadcast(entry);
    }


    private void rollAgain(TokenType tokenType) {
        captureD10roll();
        if (tokenType.equals(TokenType.DRAMA)) {
            captureD10roll();
        }
        Collections.sort(dice);
        List<Integer> dropped = new ArrayList<>();
        dropped.add(dropLowest());
        while (dice.size() > maxDice.getValue()) {
            dropped.add(dropLowest());
        }
        prepareResults(dropped, tokenType.toString());
        spendToken(tokenType);
    }

    private void handlePcUpdates() {
        sessionService.refreshPc();
        boolean hasHeroPoints = sessionService.getPc().getHeroPoints() > 0;
        postHeroRollButton.setEnabled(hasHeroPoints);
        preHeroRollButton.setEnabled(hasHeroPoints);

    }

    @Override
    protected void handleMessage(Entry entry) {
        if (EventType.PCUpdate.equals(entry.getType())) {
            if (entry.getPcId().equals(sessionService.getPc().getId())) {
                handlePcUpdates();
            }
        }
        else if (EventType.MaxDiceUpdate.equals(entry.getType())) {
            if (entry.getGameId().equals(sessionService.getGameId())) {
                maxDice.setValue(getMaxDiceForGame());
            }
        }
    }

    private Integer getMaxDiceForGame() {
        return cacheService.getGameCache().findOneById(sessionService.getGameId())
                .get().getMaxDice();
    }

}
