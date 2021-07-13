package com.smaugslair.thitracker.ui.components;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.util.RepoService;
import com.smaugslair.thitracker.util.SessionService;
import com.smaugslair.thitracker.websockets.DiceRollBroadcaster;
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

    private final RepoService repoService;
    private final SessionService sessionService;
    private Registration tlbReg;

    public DiceHistory(RepoService repoService, SessionService sessionService) {
        this.sessionService = sessionService;
        this.repoService = repoService;
        List<Entry> entryList = repoService.getEntryRepo().findAllByGameIdAndTypeEqualsOrderByIdDesc(sessionService.getGameId(), EventType.DiceRoll);
        for (Entry entry : entryList) {
            add(new Label(entry.getText()));
        }
    }



    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        tlbReg = DiceRollBroadcaster.register(newMessage -> {
            ui.access(() -> {
                if (newMessage.getGameId().equals(sessionService.getGameId())) {
                    addComponentAsFirst(new Label(newMessage.getText()));
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
