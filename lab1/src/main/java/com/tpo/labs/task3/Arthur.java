package com.tpo.labs.task3;

import java.util.Objects;

public class Arthur {
    private final String name;
    private final Head head;
    private boolean confused;
    private Console movementTarget;
    private String lastSensation;
    private String lastRealizationCause;

    public Arthur(String name) {
        this(name, new Head(false));
    }

    public Arthur(String name, Head head) {
        this.name = Objects.requireNonNull(name, "name");
        this.head = Objects.requireNonNull(head, "head");
        this.confused = false;
    }

    public void feelSensation(String sensationDescription) {
        this.lastSensation = Objects.requireNonNull(sensationDescription, "sensationDescription");
        this.confused = true;
        head.startHairMovement(1);
    }

    public void perceiveMovementTo(Console target) {
        this.movementTarget = Objects.requireNonNull(target, "target");
    }

    public void realizeTruth(String realCause) {
        this.lastRealizationCause = Objects.requireNonNull(realCause, "realCause");
        this.confused = false;
        head.stopHairMovement();
    }

    public Head getHead() {
        return head;
    }

    public String getName() {
        return name;
    }

    public boolean isConfused() {
        return confused;
    }

    public Console getMovementTarget() {
        return movementTarget;
    }

    public String getLastSensation() {
        return lastSensation;
    }

    public String getLastRealizationCause() {
        return lastRealizationCause;
    }
}
