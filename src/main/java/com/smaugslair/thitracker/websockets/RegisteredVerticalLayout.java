package com.smaugslair.thitracker.websockets;

import com.smaugslair.thitracker.data.log.Entry;
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
        if (ui.getUI().isPresent()) {
            tlbReg = Broadcaster.register(entry -> ui.access(() -> handleMessage(entry)));
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        tlbReg.remove();
        tlbReg = null;
    }

    protected abstract void handleMessage(Entry entry);
}
