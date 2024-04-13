package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.HashMap;
import java.util.Map;

@PermitAll
@PageTitle("Power Set Browser")
@Route(value = "powersetbrowser", layout = MainView.class)
public class PowerSetBrowserView extends HorizontalLayout {

    public PowerSetBrowserView(SessionService sessionService, PowersCache powersCache) {

        sessionService.getTitleBar().setTitle("PowerSet Browser");

        Tabs tabs = new Tabs();
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL, TabsVariant.LUMO_CENTERED);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setFlexGrowForEnclosedTabs(0);

        PowerSetDetails powerSetDetails = new PowerSetDetails(powersCache);
        //powerSetDetails.setWidthFull();

        Map<Tab, PowerSet> map = new HashMap<>();

        powersCache.getPowerSetList().forEach(powerSet -> {
            Tab tab = new Tab(powerSet.getName());
            tabs.add(tab);
            map.put(tab, powerSet);
        });
        tabs.addSelectedChangeListener(event -> powerSetDetails.setPowerSet(map.get(event.getSelectedTab())));
        if (!powersCache.getPowerSetList().isEmpty()) {
            powerSetDetails.setPowerSet(powersCache.getPowerSetList().first());
        }

        add(tabs, powerSetDetails);
        setAlignItems(Alignment.START);

    }

}
