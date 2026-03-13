package com.tpo.labs.lab2.trig;

public class TanFunction implements TrigonometricFunction {

    private static final double EPS = 1e-6;
    private final TrigonometricFunction sin;
    private final TrigonometricFunction cos;

    public TanFunction(TrigonometricFunction sin, TrigonometricFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public double calculate(double x) {
        double sinX = sin.calculate(x);
        double cosX = cos.calculate(x);

        if (Double.isNaN(sinX) || Double.isNaN(cosX)) {
            return Double.NaN;
        }

        if (Math.abs(cosX) < EPS) {
            return Math.copySign(Double.POSITIVE_INFINITY, sinX * cosX);
        }

        return sinX / cosX;
    }
}
