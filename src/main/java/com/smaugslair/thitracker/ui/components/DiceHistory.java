package com.smaugslair.thitracker.ui.components;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.websockets.Broadcaster;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiceHistory extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(DiceHistory.class);

    private final SessionService sessionService;
    private Registration tlbReg;

    public DiceHistory(SessionService sessionService) {
        setMargin(false);
        setSpacing(false);
        this.sessionService = sessionService;
        List<Entry> entryList = sessionService.getEntryRepo().findAllByGameIdAndTypeEqualsOrderByIdDesc(sessionService.getGameId(), EventType.DiceRoll);
        for (Entry entry : entryList) {
            add(new Label(entry.getText()));
        }
    }



    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        tlbReg = Broadcaster.register(newMessage -> {
            ui.access(() -> {
                if (newMessage.getGameId().equals(sessionService.getGameId())) {
                    log.info(newMessage.toString());
                    switch (newMessage.getType()) {
                        case DiceRoll:
                            addComponentAsFirst(new Label(newMessage.getText()));
                            break;
                        case ClearRolls:
                            removeAll();
                            break;
                    }
                }
            });
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        tlbReg.remove();
        tlbReg = null;
    }
}
