package com.smaugslair.thitracker.ui.tl;

import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.game.ActionTimeDelta;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.ui.GMTimeLineView;
import com.smaugslair.thitracker.util.AtdCache;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeltaButton extends Button {

    private final TimeLineItem item;

    private final GMTimeLineView gmTimeLineView;

    private Map<String, IntegerField> fieldMap = new HashMap<>();

    public DeltaButton(TimeLineItem item, GMTimeLineView gmTimeLineView) {
        this.item = item;
        this.gmTimeLineView = gmTimeLineView;

        FormLayout formLayout = new FormLayout();
        Integer count = 0;
        for (ActionTimeDefault atd : AtdCache.getAtds()) {
            ActionTimeDelta delta = item.getDeltas().get(atd.getName());
            if (delta.getDelta() != 0) {
                ++count;
            }
            IntegerField deltaField = new IntegerField();
            deltaField.setHasControls(true);
            deltaField.setValue(delta.getDelta());
            formLayout.addFormItem(deltaField, delta.getName());
            fieldMap.put(delta.getName(), deltaField);
        }
        setText(count.toString());

        Dialog deltaDialog = new Dialog(formLayout);

        Button confirmButton = new Button("Save", event -> {

            for (ActionTimeDefault atd : AtdCache.getAtds()) {
                ActionTimeDelta delta = item.getDeltas().get(atd.getName());
                delta.setDelta(fieldMap.get(delta.getName()).getValue());
            }
            gmTimeLineView.getTlRepo().save(item);
            deltaDialog.close();
            gmTimeLineView.refresh();
        });
        deltaDialog.add(confirmButton);
        Button cancelButton = new Button("Cancel", event -> {
            deltaDialog.close();
        });
        deltaDialog.add(cancelButton);
        deltaDialog.setWidth("400px");

        addClickListener(event -> deltaDialog.open());
    }

}
