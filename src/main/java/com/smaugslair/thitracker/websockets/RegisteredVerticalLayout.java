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

    //final Entry ping = new Entry(EventType.Ping);

/*
    ScheduledExecutorService keepAlive = Executors.newScheduledThreadPool(1);
    Runnable r = () -> {
        //log.info(this.getClass().getSimpleName()+" ---- ping -------");
        Broadcaster.broadcast(ping);
    };*/

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        //log.info("attaching "+this.getClass().getSimpleName());
        UI ui = attachEvent.getUI();
        tlbReg = Broadcaster.register(entry -> {
            ui.access(() -> {
                handleMessage(entry);
            });
        });
        //keepAlive.scheduleAtFixedRate(r, 0, 50, TimeUnit.SECONDS);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        //log.info("Detaching "+this.getClass().getSimpleName());
        tlbReg.remove();
        tlbReg = null;
        //keepAlive.shutdown();
    }

    protected abstract void handleMessage(Entry entry);
}
