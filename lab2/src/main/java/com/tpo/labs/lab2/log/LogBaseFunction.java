package com.tpo.labs.lab2.log;

public class LogBaseFunction implements LogarithmicFunction {

    private final LogarithmicFunction ln;
    private final double base;
    private final double lnBase;

    public LogBaseFunction(LogarithmicFunction ln, double base) {
        if (base <= 0.0 || base == 1.0) {
            throw new IllegalArgumentException("Base must be positive and not equal to 1");
        }
        this.ln = ln;
        this.base = base;
        this.lnBase = ln.calculate(base);
    }

    @Override
    public double calculate(double x) {
        double lnX = ln.calculate(x);
        if (Double.isNaN(lnX)) {
            return Double.NaN;
        }
        return lnX / lnBase;
    }

    public double getBase() {
        return base;
    }
}

