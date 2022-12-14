package com.smaugslair.thitracker.ui.components.ci;

import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.ui.games.CollectionView;
import com.vaadin.flow.component.button.Button;

public class DeleteButton extends Button {

    public DeleteButton(CollectedItem item, CollectionView collectionView) {
        setText("Delete");
        addClickListener(event -> collectionView.deleteItem(item));
    }
}
