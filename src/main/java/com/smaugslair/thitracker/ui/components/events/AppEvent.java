package com.smaugslair.thitracker.ui.components.events;

public class AppEvent {

    public enum AppEventType {
        HeroChosen, GameChosen, MenuChange;
    }

    private final Long objectId;


    private final AppEventType type;

    public AppEvent(AppEventType type, Long objectId) {
        this.type = type;
        this.objectId = objectId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public AppEventType getType() {
        return type;
    }
}
