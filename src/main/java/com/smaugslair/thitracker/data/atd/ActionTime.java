package com.smaugslair.thitracker.data.atd;

public class ActionTime {

    public final String name;
    public final Integer time;
    public final boolean unselected;

    public ActionTime(String name) {
        this.name = name;
        time = 0;
        unselected = true;
    }

    public ActionTime(String name, Integer time) {
        this.name = name;
        this.time = time;
        unselected = false;
    }


    @Override
    public String toString() {
        if (unselected) {
            return name;
        }
        return name + " (" + time + ')';
    }
}
