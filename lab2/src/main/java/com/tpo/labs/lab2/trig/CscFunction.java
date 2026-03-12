package com.tpo.labs.lab2.trig;

public class CscFunction implements TrigonometricFunction {

    private static final double EPS = 1e-6;
    private final TrigonometricFunction sin;

    public CscFunction(TrigonometricFunction sin) {
        this.sin = sin;
    }

    @Override
    public double calculate(double x) {
        double sinX = sin.calculate(x);

        if (Double.isNaN(sinX)) {
            return Double.NaN;
        }

        if (Math.abs(sinX) < EPS) {
            return Double.POSITIVE_INFINITY;
        }

        return 1.0 / sinX;
    }
}

