package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TraitForm extends FormLayout {

    private final static Logger log = LoggerFactory.getLogger(TraitForm.class);

    private PlayerCharacter pc;

    List<TextField> fields = new ArrayList<>(6);
    private final Button saveButton = new Button("Save");

    public TraitForm() {
        saveButton.setEnabled(false);
        for (int i = 0; i < 6; ++i) {
            TextField textField = new TextField();
            textField.setMinLength(2);
            textField.setMaxLength(64);
            textField.setRequired(true);
            textField.setRequiredIndicatorVisible(true);
            textField.addValueChangeListener(event -> isValid());
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

    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
        removeAll();

        pc.getTraits().forEach(trait -> {
            TextField field = fields.get(trait.getSortOrder()-1);
            field.setValue(trait.getName());
            addFormItem(field, trait.getType().name());
        });
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
