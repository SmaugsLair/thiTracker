package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.util.SessionService;
import com.smaugslair.thitracker.websockets.DiceRollBroadcaster;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
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


public class DiceRoller extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(DiceRoller.class);
    private final SessionService sessionService;
    private final EntryRepository entryRepository;

    private IntegerField d10s = new IntegerField();
    private IntegerField maxDice = new IntegerField();
    private FormLayout resultLayout = new FormLayout();
    private Label progressLabel = new Label();
    private ProgressBar progressBar = new ProgressBar();
    private Label expectedValue = new Label("Expected value: "+(3*5.5));
    private TextField diceText = new TextField();
    private TextField droppedText = new TextField();
    private Label droppedLabel = new Label("Dropped");

    private List<Integer> dice = new ArrayList<>();
    private int sum = 0;


    public DiceRoller(SessionService sessionService, EntryRepository entryRepository) {
        this.sessionService = sessionService;
        this.entryRepository = entryRepository;

        if (sessionService.getPc() == null) {
            add(new H3("Select a Hero first"));
            return;
        }

        FormLayout rollLayout = new FormLayout();
        rollLayout.addFormItem(new Text("Hero: "+sessionService.getPc().getName()), "Dice Roller");
        d10s.setValue(3);
        d10s.setHasControls(true);
        d10s.setMin(1);
        rollLayout.addFormItem(d10s, "d10s to roll");
        maxDice.setValue(10);
        maxDice.setHasControls(true);
        maxDice.setMin(1);
        rollLayout.addFormItem(maxDice, "Max dice (ask GM)");
        Button rollButton = new Button("Roll 3 dice");
        rollLayout.addFormItem(expectedValue, rollButton);
        d10s.addValueChangeListener(event -> {
            rollButton.setText("Roll "+event.getValue()+" dice");
            expectedValue.setText("Expected value: "+calcExpected());
        });
        maxDice.addValueChangeListener(event -> expectedValue.setText("Expected value: "+calcExpected()));
        rollButton.addClickListener(event -> normalRoll());

        add(rollLayout);

        resultLayout.setVisible(false);
        resultLayout.addFormItem(progressBar, progressLabel);
        diceText.setSizeFull();
        diceText.setReadOnly(true);
        resultLayout.addFormItem(diceText,"Dice");
        droppedText.setSizeFull();
        droppedText.setReadOnly(true);
        resultLayout.addFormItem(droppedText, droppedLabel);

        Button heroRoll = new Button("Roll 1 more, drop lowest", event -> heroRoll());
        Button dramaRoll = new Button("Roll 2 more, drop lowest", event -> dramaRoll());
        resultLayout.addFormItem(heroRoll, "Spend Hero Token");
        resultLayout.addFormItem(dramaRoll, "Spend Drama Token");

        add(resultLayout);
    }

    private int captureD10roll() {
        int roll = (int)(Math.random() * 10) + 1;
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

    private String calcExpected() {
        int dice = d10s.getValue();
        int max = maxDice.getValue();
        if (dice > max) {
            return " > " + (max *5.5);
        }
        return String.valueOf(dice * 5.5);
    }

    private void normalRoll() {
        dice.clear();
        sum = 0;
        for (int count = 0; count < d10s.getValue(); ++count) {
            captureD10roll();
        }
        Collections.sort(dice);
        List<Integer> dropped = new ArrayList<>();
        while (dice.size() > maxDice.getValue()) {
            dropped.add(dropLowest());
        }
        prepareResults(dropped, null);

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

        entryRepository.save(message);

        DiceRollBroadcaster.broadcast(message);

    }


    private void heroRoll() {
        captureD10roll();
        Collections.sort(dice);
        List<Integer> dropped = new ArrayList<>();
        dropped.add(dropLowest());
        prepareResults(dropped, "HERO");
    }

    private void dramaRoll() {
        captureD10roll();
        captureD10roll();
        Collections.sort(dice);
        List<Integer> dropped = new ArrayList<>();
        dropped.add(dropLowest());
        prepareResults(dropped, "DRAMA");
    }
}
