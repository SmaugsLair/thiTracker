package com.smaugslair.thitracker.ui.games;

import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.AbstractMainView;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.ci.DeleteButton;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.List;

@PermitAll
@Route(value = "collection", layout = MainView.class)
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
@UIScope
@Component
public class CollectionView extends AbstractMainView {



    private final SessionService sessionService;
    private final CollectedItemRepository collectedItemRepository;

    public CollectionView(SessionService sessionService, TitleBar titleBar, CollectedItemRepository collectedItemRepository) {
        super(titleBar);
        this.sessionService = sessionService;
        this.collectedItemRepository = collectedItemRepository;
        init();
    }

    private void refresh() {
        removeAll();
        init();
    }

    public void init() {

        List<CollectedItem> items = collectedItemRepository
                .findAllByGmId(sessionService.getLoggedInUser().getId());

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
        collectedItemRepository.delete(item);
        refresh();
    }

    @Override
    public String getTitle() {
        return "Collected Items";
    }
}
