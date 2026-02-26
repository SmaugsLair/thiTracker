package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerFilter;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.FilterField;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.List;

@PermitAll
@PageTitle("Power Browser")
@Route(value = "powerbrowser", layout = MainView.class)
@UIScope
@Component
public class PowerBrowserView extends Grid<Power> implements HasUrlParameter<String>, BeforeEnterListener {


    //private final static Logger log = LoggerFactory.getLogger(PowerSetBrowserView.class);
    private final TitleBar titleBar;
    private final PowersCache powersCache;

    private FilterField tagField;

    public PowerBrowserView(TitleBar titleBar, PowersCache powersCache) {
        this.titleBar = titleBar;
        this.powersCache = powersCache;
        UI.getCurrent().addBeforeEnterListener(this);
        titleBar.setTitle("Power Browser");
        init();
    }

    public void init() {

        //log.info("Initializing Power Browser");


        setHeightFull();

        boolean showBugs = false;

        for (Power power : powersCache.getPowers()) {
            if (power.isBadPrerequisite()) {
                showBugs = true;
                break;
            }
        }



        ListDataProvider<Power> dataProvider = new ListDataProvider<>(powersCache.getPowers());
        setDataProvider(dataProvider);

        PowerFilter filterObject = new PowerFilter(dataProvider);

        //setDetailsVisibleOnClick(true);
        addColumn(new NativeButtonRenderer<>(
                item -> isDetailsVisible(item) ? "-" : "+",
                item -> setDetailsVisible(item, !isDetailsVisible(item))));

        Grid.Column<Power> nameColumn =  addColumn(Power::getName).setHeader("Name").setSortable(true);
        if (showBugs) {
            addColumn(Power::isBadPrerequisite).setHeader("BUG?").setSortable(true);
        }
        Grid.Column<Power> tierColumn = addColumn(Power::getLowestTier).setHeader("Tier").setSortable(true);
        Grid.Column<Power> metaColumn = addColumn(Power::getMetaPower).setHeader("Meta").setSortable(true);
        Grid.Column<Power> tagColumn = addColumn(Power::getPowerTag).setHeader("Tag").setSortable(true);
        Grid.Column<Power> maxColumn = addColumn(Power::getMaxTaken).setHeader("Max Taken").setSortable(true);

        getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));

        List<GridSortOrder<Power>> sortByName = new GridSortOrderBuilder<Power>().thenAsc(nameColumn).build();
        sort(sortByName);

        setItemDetailsRenderer(
                new ComponentRenderer<>(power -> new PowerDetails(power, powersCache.getPowers())));

        HeaderRow filterRow = appendHeaderRow();

        filterRow.getCell(nameColumn).setComponent(new FilterField(filterObject::setName));
        filterRow.getCell(tierColumn).setComponent(new FilterField(filterObject::setTier, "50px"));
        filterRow.getCell(metaColumn).setComponent(new FilterField(filterObject::setMetaPower, "50px"));
        tagField = new FilterField(filterObject::setPowerTag);
        filterRow.getCell(tagColumn).setComponent(tagField);
        filterRow.getCell(maxColumn).setComponent(new FilterField(filterObject::setMaxTaken));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String tag) {
        if (tag != null) {
            tagField.setValue(tag);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getNavigationTarget().equals(this.getClass())) {
            //log.info("Before Enter : " + beforeEnterEvent.getNavigationTarget());
            titleBar.setTitle("Power Browser");
        }
    }
}
