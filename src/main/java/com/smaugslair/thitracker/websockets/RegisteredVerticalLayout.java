package com.smaugslair.thitracker.websockets;

import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 A VerticalLayout that registers itself with the Broadcaster
 */
public abstract class RegisteredVerticalLayout extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(RegisteredVerticalLayout.class);

    private Registration tlbReg;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        //log.info("attaching "+this.getClass().getSimpleName());
        UI ui = attachEvent.getUI();
        tlbReg = Broadcaster.register(entry -> {
            ui.access(() -> {
                if (!EventType.Ping.equals(entry.getType())) {
                    handleMessage(entry);
                }
            });
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        //log.info("Detaching "+this.getClass().getSimpleName());
        tlbReg.remove();
        tlbReg = null;
    }

    protected abstract void handleMessage(Entry entry);
}
