package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerFilter;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.FilterField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@PageTitle("Power Browser")
@Route(value = "powerbrowser", layout = MainView.class)
public class PowerBrowserView extends Grid<Power> {

    public PowerBrowserView(SessionService sessionService, PowersCache powersCache) {

        sessionService.getTitleBar().setTitle("Power Browser");
        setHeightFull();

        boolean showBugs = false;

        for (Power power : powersCache.getPowers()) {
            if (power.isBadPrerequisite()) {
                showBugs = true;
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
        filterRow.getCell(tagColumn).setComponent(new FilterField(filterObject::setPowerTag));
        filterRow.getCell(maxColumn).setComponent(new FilterField(filterObject::setMaxTaken));
    }
}
