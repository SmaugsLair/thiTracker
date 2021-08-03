package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class NewItemForm extends FormLayout {
    private final RadioButtonGroup<String> radioGroup;
    private final Select<PlayerCharacter> pcField;
    private final TextField otherField;
    private final IntegerField stunField;
    private final IntegerField timeField;
    private final Checkbox hidden;

    public NewItemForm(List<PlayerCharacter> pcs, SessionService sessionService) {

        radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("PC", "Other");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);


        Div radioFields = new Div();
        pcField = new Select<>();
        pcField.setItemLabelGenerator(item -> item.getCharacterAndPlayerName(
                sessionService.getUserRepository().findById(item.getUserId()).orElse(new User())));

        otherField = new TextField();
        otherField.setPlaceholder("Other");

        // = pcRepo.findAllByUserIdAndGameIdIsNull(SecurityUtils.getLoggedInUser().getId());
        pcField.setItems(pcs);

        if (pcs.isEmpty()) {
            radioGroup.setValue("Other");
            radioFields.add(otherField);
            radioGroup.setItemEnabledProvider(item -> !"PC".equals(item));
        }
        else {
            radioGroup.setValue("PC");
            radioFields.add(pcField);
        }


        radioGroup.addValueChangeListener(event -> {
            if ("PC".equals(event.getValue())) {
                radioFields.remove(otherField);
                radioFields.add(pcField);
            }
            else {
                radioFields.remove(pcField);
                radioFields.add(otherField);
            }
        });
        addFormItem(radioFields, radioGroup);

        stunField = new IntegerField();
        stunField.setHasControls(true);
        stunField.setValue(0);
        stunField.setMin(0);
        addFormItem(stunField, "Stun");

        timeField = new IntegerField();
        timeField.setHasControls(true);
        timeField.setValue(0);
        addFormItem(timeField, "Time");

        hidden = new Checkbox();
        hidden.setValue(true);
        addFormItem(hidden, "Hidden");
    }

    public boolean isPC() {
        return radioGroup.getValue().equals("PC");
    }

    public PlayerCharacter getPC() {
        return pcField.getValue();
    }

    public String getName() {
        return otherField.getValue();
    }

    public Integer getStun() {
        return stunField.getValue();
    }

    public Integer getTime() {
        return timeField.getValue();
    }

    public Boolean getHidden() {
        return hidden.getValue();
    }
}
