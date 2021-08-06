package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.ui.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.Map;

@PageTitle("Power Set Browser")
@Route(value = "powersetbrowser", layout = MainView.class)
public class PowerSetBrowserView extends VerticalLayout {

    public PowerSetBrowserView(PowersCache powersCache) {

        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        PowerSetDetails powerSetDetails = new PowerSetDetails(powersCache);
        tabs.setFlexGrowForEnclosedTabs(0);
        //powerSetDetails.setWidthFull();

        Map<Tab, PowerSet> map = new HashMap<>();

        powersCache.getPowerSetList().forEach(powerSet -> {
            Tab tab = new Tab(powerSet.getName());
            tabs.add(tab);
            map.put(tab, powerSet);
        });
        tabs.addSelectedChangeListener(event -> powerSetDetails.setPowerSet(map.get(event.getSelectedTab())));
        if (!powersCache.getPowerSetList().isEmpty()) {
            powerSetDetails.setPowerSet(powersCache.getPowerSetList().get(0));
        }
        add(tabs, powerSetDetails);

    }

}
