package com.tpo.labs.task3;

public class DomainModelSimulation {
    public static void main(String[] args) {
        Arthur arthur = new Arthur("Артур");
        Console console = new Console();
        Camera camera = new Camera();

        boolean performed = camera.performDollyIn(arthur, console);

        System.out.println("performed=" + performed);
        System.out.println("camera.zoomLevel=" + camera.getZoomLevel());
        System.out.println("arthur.confused=" + arthur.isConfused());
        System.out.println("arthur.movementTarget=" + arthur.getMovementTarget());
    }
}
