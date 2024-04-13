package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.pc.AbilityScore;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.smaugslair.thitracker.data.pc.TraitType;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.smaugslair.thitracker.websockets.RegisteredVerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class DiceRoller extends RegisteredVerticalLayout implements AbilityChoice {

    private final String exp_val = "Exp val: ";


    private enum TokenType {HERO, DRAMA}

    private static final Logger log = LoggerFactory.getLogger(DiceRoller.class);

    private final SessionService sessionService;

    private AbilityScore abilityScore;

    private final IntegerField d10s = new IntegerField();
    private final IntegerField maxDice = new IntegerField();
    private final VerticalLayout resultLayout = new VerticalLayout();
    private final IntegerField result = new IntegerField();
    private final ProgressBar progressBar = new ProgressBar();
    private final Span expectedValue = new Span(exp_val+(3*5.5));
    private final Span expectedHeroValue = new Span(exp_val+(4*5.5));
    private final TextField diceText = new TextField();
    private final TextField droppedText = new TextField();
    private final Span droppedLabel = new Span("Dropped");

    private final Button preHeroRollButton = new UserSafeButton("Hero roll 4");

    private final Dialog traitRollDialog = new Dialog();

    final Button postHeroRollButton = new UserSafeButton("Roll 1 more, drop lowest",
            event -> showTraitRollDialog(TraitType.Hero, false)); //rollAgain(TokenType.HERO));


    private final List<Integer> dice = new ArrayList<>();
    private int sum = 0;


    public DiceRoller(SessionService sessionService) {
        this.sessionService = sessionService;


        //setPadding(false);
        setMargin(false);
        setSpacing(false);
        add(new Span("Waiting for abilty choice"));
    }

    private void init() {
        removeAll();
        PlayerCharacter pc = sessionService.getPc();
        if (pc == null) {
            add(new Span("No Hero loaded"));
            return;
        }


        FormLayout topLayout = new FormLayout();
        topLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        TextField heroField = new TextField();
        heroField.setWidth("120px");
        heroField.setValue(pc.getName());
        heroField.setReadOnly(true);
        topLayout.addFormItem(heroField,"Hero");

        TextField abilityField = new TextField();
        abilityField.setWidth("120px");
        abilityField.setValue(abilityScore.getAbility().getDisplayName());
        abilityField.setReadOnly(true);
        topLayout.addFormItem(abilityField, "Ability");

        maxDice.setValue(getMaxDiceForGame());
        maxDice.setWidth("120px");
        maxDice.setReadOnly(true);
        maxDice.setStepButtonsVisible(false);
        topLayout.addFormItem(maxDice, "Max dice");

        //topLayout.addFormItem(new Span(), "");



        //rollLayout.addFormItem(new Span("Hero: "+pc.getName()), "Dice Roller");
        d10s.setValue(Math.min(getMaxDiceForGame(), abilityScore.getPoints()));
        d10s.setWidth("120px");
        d10s.setStepButtonsVisible(true);
        d10s.setMin(1);
        d10s.setMax(getMaxDiceForGame());
        topLayout.addFormItem(d10s, "Dice to roll");

        Button rollButton = new UserSafeButton("Roll " +d10s.getValue());
        topLayout.addFormItem(rollButton, expectedValue);

        preHeroRollButton.setEnabled(pc.isHeroPointsAvailable());
        topLayout.addFormItem(preHeroRollButton, expectedHeroValue);


       /* VerticalLayout normal = new VerticalLayout();
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
        hero.setSpacing(false);*/
/*
        normal.add(rollButton, expectedValue);
        hero.add(preHeroRollButton, expectedHeroValue);

        rollLayout.addFormItem(hero, normal);*/



        d10s.addValueChangeListener(event -> {
            rollButton.setText("Roll "+d10s.getValue());
            expectedValue.setText(exp_val+calcExpected(d10s.getValue()));

            preHeroRollButton.setText("Hero roll "+(d10s.getValue()+1));
            expectedHeroValue.setText(exp_val+calcExpected(d10s.getValue()+1));
        });
        /*maxDice.addValueChangeListener(event -> {
            expectedValue.setText(exp_val+calcExpected(d10s.getValue()));
            expectedHeroValue.setText(exp_val+calcExpected(d10s.getValue()+1));
        });*/

        rollButton.addClickListener(event -> initialRoll(null));
        preHeroRollButton.addClickListener(event -> showTraitRollDialog(TraitType.Hero, true));

        add(topLayout);

        resultLayout.setVisible(false);
        resultLayout.removeAll();
        resultLayout.add(progressBar);
        FormLayout resultsForm = new FormLayout();
        resultLayout.add(resultsForm);
        add(resultLayout);
        resultsForm.addFormItem(result, "Result");
        diceText.setSizeFull();
        diceText.setReadOnly(true);
        resultsForm.addFormItem(diceText,"Dice");
        droppedText.setSizeFull();
        droppedText.setReadOnly(true);
        resultsForm.addFormItem(droppedText, droppedLabel);

        Button dramaRoll = new UserSafeButton("Roll 2 more, drop lowest",
                event -> showTraitRollDialog(TraitType.Drama, false));
        resultsForm.addFormItem(postHeroRollButton, "Spend Hero Token");
        postHeroRollButton.setEnabled(pc.isHeroPointsAvailable());
        resultsForm.addFormItem(dramaRoll, "Spend Drama Token");

        add(resultLayout);
    }

    private void captureD10roll() {
        int roll = (int)(Math.random() * 10) + 1;
        sum += roll;
        dice.add(roll);
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

    private void initialRoll(Trait heroTrait) {
        dice.clear();
        sum = 0;
        int total = d10s.getValue();
        if (heroTrait != null) {
            ++total;
        }
        for (int count = 0; count < total; ++count) {
            captureD10roll();
        }
        Collections.sort(dice);
        List<Integer> dropped = new ArrayList<>();
        while (dice.size() > maxDice.getValue()) {
            dropped.add(dropLowest());
        }
        prepareResults(dropped, heroTrait);

        if (heroTrait != null) {
            spendToken(heroTrait);
        }

        resultLayout.setVisible(true);

    }

    private void prepareResults(List<Integer> dropped, Trait trait) {

        double percent = calcPercent(sum, d10s.getValue(), maxDice.getValue());
        progressBar.setValue(percent);
        ProgressBarVariant variant = ProgressBarVariant.LUMO_SUCCESS;
        if (percent < .55) {
            variant = ProgressBarVariant.LUMO_ERROR;
        }
        progressBar.removeThemeVariants(ProgressBarVariant.values());
        progressBar.addThemeVariants(variant);
        result.setValue(sum);
        diceText.setValue(dice.toString());
        droppedLabel.setVisible(!dropped.isEmpty());
        droppedText.setVisible(!dropped.isEmpty());
        droppedText.setValue(dropped.toString());

        PlayerCharacter pc = sessionService.getPc();
        StringBuilder sb = new StringBuilder();
        sb.append(pc.getName()).append(" rolled ").append(sum).append(" on ").append(abilityScore.getAbility().getDisplayName());
        sb.append("  ").append(dice);
        if (trait != null) {
            sb.append(" ").append(trait.getType()).append(":").append(trait.getName());
        }


        Entry message = new Entry();
        message.setGameId(sessionService.getGameId());
        message.setType(EventType.DiceRoll);
        message.setText(sb.toString());

        sessionService.getEntryRepo().save(message);

        Broadcaster.broadcast(message);

    }

    private void spendToken(Trait trait) {

        PlayerCharacter pc = sessionService.getPc();
        if (TraitType.Hero.equals(trait.getType())) {
            trait.setPoints(trait.getPoints()-1);
        }
        else {
            trait.setPoints(trait.getPoints()+1);
        }
        pc = sessionService.getPcRepo().save(pc);
        sessionService.setPc(pc);
        Entry entry = new Entry();
        entry.setPcId(pc.getId());
        entry.setGameId(sessionService.getGameId());
        entry.setType(EventType.PCUpdate);
        Broadcaster.broadcast(entry);
    }


    private void rollAgain(Trait trait) {
        captureD10roll();
        if (TraitType.Drama.equals(trait.getType())) {
            captureD10roll();
        }
        Collections.sort(dice);
        List<Integer> dropped = new ArrayList<>();
        dropped.add(dropLowest());
        while (dice.size() > maxDice.getValue()) {
            dropped.add(dropLowest());
        }
        prepareResults(dropped, trait);
        spendToken(trait);
    }

    private void handlePcUpdates() {
        sessionService.refreshPc();
        boolean hasHeroPoints = sessionService.getPc().isHeroPointsAvailable();
        postHeroRollButton.setEnabled(hasHeroPoints);
        preHeroRollButton.setEnabled(hasHeroPoints);

    }

    @Override
    protected void handleMessage(Entry entry) {

        switch (entry.getType()) {
            case PCUpdate:
                if (entry.getPcId().equals(sessionService.getPc().getId())) {
                    handlePcUpdates();
                }
                break;
            case MaxDiceUpdate:
                if (entry.getGameId().equals(sessionService.getGameId())) {
                    maxDice.setValue(getMaxDiceForGame());
                }
                break;
        }

    }

    private Integer getMaxDiceForGame() {
        Optional<Game> game = sessionService.getGameRepo().findById(sessionService.getGameId());
        return game.isPresent() ? game.get().getMaxDice() : 10;
    }

    private void showTraitRollDialog(TraitType type, boolean initial) {
        traitRollDialog.removeAll();
        VerticalLayout buttonColumn = new VerticalLayout();
        traitRollDialog.add(buttonColumn);
        PlayerCharacter pc = sessionService.getPc();
        pc.getTraits().stream().filter(trait -> trait.getType().equals(type)).forEach(trait -> {
            //Drama always, Hero if points available
            if (TraitType.Drama.equals(trait.getType()) || trait.getPoints() > 0) {
                buttonColumn.add(new UserSafeButton("Spend " + trait.getName() + " point", event -> {
                    if (initial) {
                        initialRoll(trait);
                    }
                    else {
                        rollAgain(trait);
                    }
                    traitRollDialog.close();
                }));
            }
        });
        traitRollDialog.open();
    }


    @Override
    public void setChoice(AbilityScore abilityScore) {
        this.abilityScore = abilityScore;
        init();
    }
}
