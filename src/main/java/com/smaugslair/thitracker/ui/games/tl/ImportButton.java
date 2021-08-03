package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.game.ActionTimeDelta;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.user.CollectedDelta;
import com.smaugslair.thitracker.data.user.CollectedItem;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.button.Button;

public class ImportButton extends Button {

    public ImportButton(CollectedItem item, GMTimeLineView gmTimeLineView) {
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
            gmTimeLineView.getTliCache().save(tli);
            gmTimeLineView.refreshAndBroadcast();
        });
    }
}
