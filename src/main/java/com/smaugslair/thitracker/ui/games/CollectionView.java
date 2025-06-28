package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.ci.DeleteButton;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@Route(value = "collection", layout = MainView.class)
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class CollectionView extends VerticalLayout {

    private final SessionService sessionService;

    public CollectionView(SessionService sessionService) {
        this.sessionService = sessionService;
        init();
        sessionService.getTitleBar().setTitle("Collection");
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        List<CollectedItem> items = sessionService.getCiRepo()
                .findAllByGmId(SecurityUtils.getLoggedInUser(sessionService).getId());

        if (items.isEmpty()) {
            add("Empty Collection - Items can be added from an active timeline");
            return;
        }

        Grid<CollectedItem> grid = new Grid<>();
        grid.setThemeName("min-padding");
        grid.setItems(items);
        grid.setAllRowsVisible(true);
        grid.setClassNameGenerator(item -> item.getColor());
        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        grid.addColumn(CollectedItem::getName);
        grid.addComponentColumn((CollectedItem item1) -> new DeleteButton(item1, this));
        grid.setWidth("500px");

        add(grid);

    }

    public void deleteItem(CollectedItem item) {
        sessionService.getCiRepo().delete(item);
        refresh();
    }
}
