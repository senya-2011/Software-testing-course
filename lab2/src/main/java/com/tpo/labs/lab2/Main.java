package com.tpo.labs.lab2;

public class Main {
    public static void main(String[] args) {
        SystemOfFunctions system = new SystemOfFunctions();

        double[] xs = new double[]{-1.0, -0.5, 0.0, 0.1, 0.5, 1.0};
        for (double x : xs) {
            System.out.println("x=" + x + " -> f(x)=" + system.calculate(x));
        }
    }
}

