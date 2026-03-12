package com.tpo.labs.lab2.trig;

public class SecFunction implements TrigonometricFunction {

    private static final double EPS = 1e-6;
    private final TrigonometricFunction cos;

    public SecFunction(TrigonometricFunction cos) {
        this.cos = cos;
    }

    @Override
    public double calculate(double x) {
        double cosX = cos.calculate(x);

        if (Double.isNaN(cosX)) {
            return Double.NaN;
        }

        if (Math.abs(cosX) < EPS) {
            return Double.POSITIVE_INFINITY;
        }

        return 1.0 / cosX;
    }
}

