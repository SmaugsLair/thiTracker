package com.smaugslair.thitracker.websockets;

import com.smaugslair.thitracker.data.log.Entry;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TimelineBroadcaster {
    private static Logger log = LoggerFactory.getLogger(TimelineBroadcaster.class);
    private static Executor executor = Executors.newSingleThreadExecutor();

    private static LinkedList<Consumer<Entry>> listeners = new LinkedList<>();

    public static synchronized Registration register(Consumer<Entry> listener) {
        listeners.add(listener);
        return () -> {
            synchronized (TimelineBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void broadcast(Entry message) {
        for (Consumer<Entry> listener : listeners) {
            executor.execute(() -> listener.accept(message));
        }
    }
}