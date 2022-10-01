package com.smaugslair.thitracker.ui.sheet;

public class AvailableTaken {

    private boolean available = false;
    private int timesTaken = 0;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getTimesTaken() {
        return timesTaken;
    }

    public void setTimesTaken(int timesTaken) {
        this.timesTaken = timesTaken;
    }
}
