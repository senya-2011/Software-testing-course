package com.tpo.labs.task3;

import java.util.Objects;

public class Console {
    private final String description;
    private boolean active;

    public Console() {
        this("Пульт");
    }

    public Console(String description) {
        this.description = Objects.requireNonNull(description, "description");
        this.active = true;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return description;
    }
}
