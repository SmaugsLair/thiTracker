package com.smaugslair.thitracker.ui.powers;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerFilter;
import com.smaugslair.thitracker.services.PowersCache;
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

import java.util.List;

@PageTitle("Power Browser")
@Route(value = "powerbrowser", layout = MainView.class)
public class PowerBrowserView extends Grid<Power> {

    public PowerBrowserView(PowersCache powersCache) {

        setHeightFull();

        ListDataProvider<Power> dataProvider = new ListDataProvider<>(powersCache.getPowerList());
        setDataProvider(dataProvider);

        PowerFilter filterObject = new PowerFilter();
        dataProvider.setFilter(power -> filterObject.test(power));

        //setDetailsVisibleOnClick(true);
        addColumn(new NativeButtonRenderer<>(
                item -> isDetailsVisible(item) ? "-" : "+",
                item -> setDetailsVisible(item, !isDetailsVisible(item))));
       /* addColumn(new ComponentRenderer<>(power -> {
            return new Button("+", event -> {
                boolean visible = !isDetailsVisible(power);
                setDetailsVisible(power, visible);
                event.getSource().setText(visible ? "-" : "+");
            });
        }));*/

        Grid.Column<Power> nameColumn =  addColumn(Power::getName).setHeader("Name").setSortable(true);
        Grid.Column<Power> tierColumn = addColumn(Power::getTier).setHeader("Tier").setSortable(true);
        Grid.Column<Power> metaColumn = addColumn(Power::getMetaPower).setHeader("Meta").setSortable(true);
        Grid.Column<Power> tagColumn = addColumn(Power::getPowerTag).setHeader("Tag").setSortable(true);
        Grid.Column<Power> maxColumn = addColumn(Power::getMaxTaken).setHeader("Max Taken").setSortable(true);

        getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));

        List<GridSortOrder<Power>> sortByName = new GridSortOrderBuilder<Power>().thenAsc(nameColumn).build();
        sort(sortByName);

        setItemDetailsRenderer(new ComponentRenderer<>(power -> {
            return new PowerDetails(power);
        }));

        HeaderRow filterRow = appendHeaderRow();

        FilterField nameFilterField = new FilterField();
        nameFilterField.addValueChangeListener(event -> {
            filterObject.setName(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(nameColumn).setComponent(nameFilterField);

        FilterField tierFilterField = new FilterField();
        tierFilterField.setWidth("50px");
        tierFilterField.addValueChangeListener(event -> {
            filterObject.setTier(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(tierColumn).setComponent(tierFilterField);


        FilterField metaFilterField = new FilterField();
        metaFilterField.setWidth("50px");
        metaFilterField.addValueChangeListener(event -> {
            filterObject.setMetaPower(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(metaColumn).setComponent(metaFilterField);


        FilterField tagFilterField = new FilterField();
        tagFilterField.addValueChangeListener(event -> {
            filterObject.setPowerTag(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(tagColumn).setComponent(tagFilterField);


        FilterField maxFilterField = new FilterField();
        maxFilterField.setWidth("50px");
        maxFilterField.addValueChangeListener(event -> {
            filterObject.setMaxTaken(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(maxColumn).setComponent(maxFilterField);
    }
}
