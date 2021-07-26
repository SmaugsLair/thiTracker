package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.game.ActionTimeDelta;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import java.util.HashMap;
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
        for (ActionTimeDefault atd : gmTimeLineView.getAtds()) {
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

        ConfirmDialog deltaDialog = new ConfirmDialog(formLayout);

        Button confirmButton = new Button("Save", event -> {

            for (ActionTimeDefault atd : gmTimeLineView.getAtds()) {
                ActionTimeDelta delta = item.getDeltas().get(atd.getName());
                delta.setDelta(fieldMap.get(delta.getName()).getValue());
            }
            gmTimeLineView.getTliCache().save(item);
            deltaDialog.close();
            gmTimeLineView.refreshAndBroadcast();
        });
        deltaDialog.setConfirmButton(confirmButton);
        deltaDialog.setWidth("400px");

        addClickListener(event -> deltaDialog.open());
    }

}
