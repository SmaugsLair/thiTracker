package com.smaugslair.thitracker.ui.ci;

import com.smaugslair.thitracker.data.game.ActionTimeDelta;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.user.CollectedDelta;
import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.ui.GMTimeLineView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

public class ImportButton extends Button {
    private final CollectedItem item;
    private final GMTimeLineView gmTimeLineView;

    public ImportButton(CollectedItem item, GMTimeLineView gmTimeLineView) {
        this.item = item;
        this.gmTimeLineView = gmTimeLineView;
        setText("Import");
        addClickListener(event -> {
            TimeLineItem tli = new TimeLineItem();
            tli.setName(item.getName());
            tli.setColor(item.getColor());
            tli.setTime(0);
            tli.setStun(0);
            tli.setHidden(true);
            tli.setGameId(gmTimeLineView.getGameID());
            for(CollectedDelta delta : item.getDeltas()) {
                ActionTimeDelta atd = new ActionTimeDelta();
                atd.setName(delta.getName());
                atd.setDelta(delta.getDelta());
                atd.setTimeLineItem(tli);
                tli.getDeltas().put(delta.getName(), atd);
            }
            gmTimeLineView.getTlRepo().save(tli);
            gmTimeLineView.refresh();
        });
    }
}
