package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.services.SessionService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.Map;

public class PowerSetDetails extends VerticalLayout {

    private final Map<String, PowerSetDetail> clutchDetailMap = new HashMap<>();

    public PowerSetDetails(SessionService sessionService, PowersCache powersCache) {
        powersCache.getPowerSetList().forEach(powerSet -> add(new PowerSetDetail(sessionService, powerSet, powersCache)));
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
