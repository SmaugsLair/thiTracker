package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.Trait;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TraitForm extends FormLayout {

    private final static Logger log = LoggerFactory.getLogger(TraitForm.class);

    private final PlayerCharacter pc;

    List<TextField> fields = new ArrayList<>(6);
    private final Button saveButton = new Button("Save");

    public TraitForm(PlayerCharacter pc) {
        this.pc = pc;
        init();
    }

    private void init() {
        //List<Trait> traits = pc.getTraits().stream().sorted().collect(Collectors.toList());
        saveButton.setEnabled(false);
        for (int i = 0; i < 6; ++i) {
            Trait trait = pc.getTraits().get(i);
            TextField textField = new TextField();
            textField.setValue(trait.getName());
            textField.setMinLength(2);
            textField.setMaxLength(20);
            textField.setRequired(true);
            textField.setRequiredIndicatorVisible(true);
            textField.addValueChangeListener(event -> isValid());
            addFormItem(textField, trait.getType().name());
            fields.add(textField);
        }
    }

    public boolean isValid() {
        for (TextField textField: fields) {
            if (textField.isInvalid()) {
                saveButton.setEnabled(false);
                return false;
            }
        }
        saveButton.setEnabled(true);
        return true;
    }

    public PlayerCharacter getPC() {
        if (!isValid()) {
            return null;
        }
        for (int i = 0; i < 6; ++i) {
            pc.getTraits().get(i).setName(fields.get(i).getValue());
        }
        return pc;

    }

    public Button getSaveButton() {
        return saveButton;
    }
}
