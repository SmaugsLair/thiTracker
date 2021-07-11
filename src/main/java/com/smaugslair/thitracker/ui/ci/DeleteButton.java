package com.smaugslair.thitracker.ui.ci;

import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.ui.CollectionView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

public class DeleteButton extends Button {
    private final CollectedItem item;
    private final CollectionView collectionView;

    public DeleteButton(CollectedItem item, CollectionView collectionView) {
        this.item = item;
        this.collectionView = collectionView;
        setText("Delete");
        addClickListener(event -> collectionView.deleteItem(item));
    }
}
