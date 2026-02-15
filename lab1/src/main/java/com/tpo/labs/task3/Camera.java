package com.tpo.labs.task3;

import java.util.Objects;

public class Camera {
    private boolean recording;
    private int zoomLevel;

    public Camera() {
        this(true);
    }

    public Camera(boolean recording) {
        this.recording = recording;
        this.zoomLevel = 1;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public boolean performDollyIn(Arthur subject, Console target) {
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(target, "target");

        if (!recording || !target.isActive()) {
            return false;
        }

        this.zoomLevel++;

        subject.feelSensation("волосы на голове зашевелились");
        subject.perceiveMovementTo(target);
        subject.realizeTruth("наезд камеры при съемке");
        return true;
    }
}
