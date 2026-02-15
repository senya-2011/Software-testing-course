package com.tpo.labs;

public class SecCalculator {

    private static final double EPS = 1e-6;
    private static final int MAX_ITERATIONS = 10000;

    public static double sec(double x){

        double cosX = calculateCos(x);

        if (Math.abs(cosX) < EPS) {
            return Double.POSITIVE_INFINITY;
        }

        return 1.0 / cosX;
    }

    private static double calculateCos(double x){
        x = x % (2 * Math.PI);

        double sum = 1.0;
        double term = 1.0;
        int n = 1;

        while (Math.abs(term) > EPS && n < MAX_ITERATIONS){
            term = -term * x * x / ((2 * n -1) * (2 * n));
            sum += term;
            n++;
        }

        return sum;
    }
}
