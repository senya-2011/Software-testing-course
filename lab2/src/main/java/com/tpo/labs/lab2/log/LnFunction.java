package com.tpo.labs.lab2.log;

public class LnFunction implements LogarithmicFunction {

    private static final double EPS = 1e-10;
    private static final int MAX_ITERATIONS = 200_000;
    private static final double LN2 = 0.6931471805599453;

    @Override
    public double calculate(double x) {
        if (Double.isNaN(x) || x <= 0.0) {
            return Double.NaN;
        }
        if (Double.isInfinite(x)) {
            return Double.POSITIVE_INFINITY;
        }

        int k = 0;
        double y = x;
        while (y > 2.0) {
            y /= 2.0;
            k++;
        }
        while (y < 0.5) {
            y *= 2.0;
            k--;
        }

        double t = (y - 1) / (y + 1);
        double term = t;
        double sum = 0.0;
        int n = 1;

        while (Math.abs(term) > EPS && n < MAX_ITERATIONS) {
            sum += term / n;
            term *= t * t;
            n += 2;
        }

        double lnY = 2 * sum;
        return lnY + k * LN2;
    }
}
