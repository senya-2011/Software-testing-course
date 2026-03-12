package com.tpo.labs.lab2;

import com.tpo.labs.lab2.log.LnFunction;
import com.tpo.labs.lab2.log.LogarithmicFunction;
import com.tpo.labs.lab2.log.LogBaseFunction;
import com.tpo.labs.lab2.trig.TrigonometricFunction;
import com.tpo.labs.lab2.trig.*;

public class SystemOfFunctions {

    private final TrigonometricFunction cos;
    private final TrigonometricFunction sin;
    private final TrigonometricFunction sec;
    private final TrigonometricFunction csc;
    private final TrigonometricFunction tan;
    private final TrigonometricFunction cot;

    private final LogarithmicFunction ln;
    private final LogarithmicFunction log2;
    private final LogarithmicFunction log3;
    private final LogarithmicFunction log5;

    public SystemOfFunctions() {
        CosFunction cos = new CosFunction();
        SinFunction sin = new SinFunction();
        LnFunction ln = new LnFunction();

        this.cos = cos;
        this.sin = sin;
        this.sec = new SecFunction(cos);
        this.csc = new CscFunction(sin);
        this.tan = new TanFunction(sin, cos);
        this.cot = new CotFunction(sin, cos);

        this.ln = ln;
        this.log2 = new LogBaseFunction(ln, 2.0);
        this.log3 = new LogBaseFunction(ln, 3.0);
        this.log5 = new LogBaseFunction(ln, 5.0);
    }

    public SystemOfFunctions(
            TrigonometricFunction cos,
            TrigonometricFunction sin,
            TrigonometricFunction sec,
            TrigonometricFunction csc,
            TrigonometricFunction tan,
            TrigonometricFunction cot,
            LogarithmicFunction ln,
            LogarithmicFunction log2,
            LogarithmicFunction log3,
            LogarithmicFunction log5
    ) {
        this.cos = cos;
        this.sin = sin;
        this.sec = sec;
        this.csc = csc;
        this.tan = tan;
        this.cot = cot;
        this.ln = ln;
        this.log2 = log2;
        this.log3 = log3;
        this.log5 = log5;
    }

    public double calculate(double x) {
        if (x <= 0.0) {
            return trigonometricBranch(x);
        } else {
            return logarithmicBranch(x);
        }
    }

    private double trigonometricBranch(double x) {
        double secX = sec.calculate(x);
        double cscX = csc.calculate(x);
        double tanX = tan.calculate(x);
        double cotX = cot.calculate(x);
        double sinX = sin.calculate(x);
        double cosX = cos.calculate(x);

        double left = pow(
                pow(
                        pow(
                                pow(
                                        (pow((secX * cscX) / pow(tanX, 2), 3) - (cscX - tanX)),
                                        3
                                ),
                                2
                        ),
                        3
                ),
                3
        );

        double inner = (cosX - secX) - (pow(cscX, 3) * (pow(sinX, 2) * secX));
        double factor1 = (tanX + tanX) * inner;
        double factor2 = (cosX / cotX) + (cotX * (cotX - tanX));

        double denom = cscX + (factor1 * factor2);
        return left / denom;
    }

    private double logarithmicBranch(double x) {
        double log2X = log2.calculate(x);
        double log3X = log3.calculate(x);
        double log5X = log5.calculate(x);
        double lnX = ln.calculate(x);

        return pow(((log2X / log5X) - lnX) / log3X, 3) / log3X;
    }

    private static double pow(double value, int power) {
        if (power == 0) {
            return 1.0;
        }
        double result = 1.0;
        int p = Math.abs(power);
        for (int i = 0; i < p; i++) {
            result *= value;
        }
        return power > 0 ? result : 1.0 / result;
    }
}

