package com.tpo.labs.task3;

public class Head {
    private final boolean corporeal;
    private boolean hairMoving;
    private int hairMovementLevel;

    public Head(boolean corporeal) {
        this.corporeal = corporeal;
        this.hairMoving = false;
        this.hairMovementLevel = 0;
    }

    public boolean isCorporeal() {
        return corporeal;
    }

    public boolean isHairMoving() {
        return hairMoving;
    }

    public int getHairMovementLevel() {
        return hairMovementLevel;
    }

    public void startHairMovement(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("level must be positive");
        }
        this.hairMoving = true;
        this.hairMovementLevel = level;
    }

    public void stopHairMovement() {
        this.hairMoving = false;
        this.hairMovementLevel = 0;
    }

    @Override
    public String toString() {
        return (corporeal ? "Обычная" : "Бесплотная") + " голова";
    }
}
