package com.tpo.labs.lab2.trig;

public class SinFunction implements TrigonometricFunction {

    private static final double EPS = 1e-10;
    private static final int MAX_ITERATIONS = 200_000;

    @Override
    public double calculate(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }

        x = x % (2 * Math.PI);

        double sum = x;
        double term = x;
        int n = 1;

        while (Math.abs(term) > EPS && n < MAX_ITERATIONS) {
            term = -term * x * x / ((2 * n) * (2 * n + 1));
            sum += term;
            n++;
        }

        return sum;
    }
}

