package com.tpo.labs.lab2.trig;

public class CosFunction implements TrigonometricFunction {
    private final TrigonometricFunction sin;

    public CosFunction() {
        this(new SinFunction());
    }

    public CosFunction(TrigonometricFunction sin) {
        this.sin = sin;
    }

    @Override
    public double calculate(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }
        return sin.calculate((Math.PI / 2.0) - x);
    }
}
