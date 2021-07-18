package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.util.PowersCache;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerSetDetails extends VerticalLayout {

    private Map<String, PowerSetDetail> clutchDetailMap = new HashMap<>();

    public PowerSetDetails(PowersCache powersCache) {
        powersCache.getPowerSetList().forEach(powerSet -> add(new PowerSetDetail(powerSet, powersCache)));
        getChildren().forEach(component -> component.setVisible(false));
    }

    public void setPowerSet(PowerSet powerSet) {
        getChildren().forEach(component -> {
            if (component.getId().isPresent()) {
                component.setVisible(component.getId().get().equals(powerSet.getName()));
            }
        });
    }
}
