package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.AbstractMainView;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@PermitAll
@PageTitle("Power Set Browser")
@Route(value = "powersetbrowser", layout = MainView.class)
@UIScope
@Component
public class PowerSetBrowserView extends AbstractMainView implements BeforeEnterListener {

    //private final static Logger log = LoggerFactory.getLogger(PowerSetBrowserView.class);

    private final PowersCache powersCache;

    public PowerSetBrowserView(TitleBar titleBar, PowersCache powersCache) {
        super(titleBar);
        this.powersCache = powersCache;
        init();
    }

    public void init() {
        //log.info("PowerSetBrowserView init");

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);
        mainLayout.setPadding(false);
        add(mainLayout);


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

        mainLayout.add(tabs, powerSetDetails);
        setAlignItems(Alignment.START);

    }


    @Override
    public String getTitle() {
        return "PowerSet Browser";
    }

}
