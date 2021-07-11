package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.ci.DeleteButton;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "collection", layout = MainView.class)
public class CollectionView extends VerticalLayout {

    private final CollectedItemRepository ciRepo;

    public CollectionView(CollectedItemRepository collectedItemRepository) {
        ciRepo = collectedItemRepository;
        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        add(new H3("Collection"));
        List<CollectedItem> items = ciRepo.findAllByGmId(SecurityUtils.getLoggedInUser().getId());

        if (items.isEmpty()) {
            add("Empty Collection - Items can be added from an active timeline");
            return;
        }

        Grid<CollectedItem> grid = new Grid<>();
        grid.setItems(items);
        grid.setHeightByRows(true);
        grid.setClassNameGenerator(item -> "w3-"+item.getColor());
        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        grid.addColumn(CollectedItem::getName);
        grid.addComponentColumn((CollectedItem item1) -> new DeleteButton(item1, this));
        grid.setWidth("500px");

        add(grid);

    }

    public void deleteItem(CollectedItem item) {
        ciRepo.delete(item);
        refresh();
    }
}
