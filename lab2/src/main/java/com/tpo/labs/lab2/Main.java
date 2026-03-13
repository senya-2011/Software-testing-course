package com.tpo.labs.lab2;

import com.tpo.labs.lab2.io.CsvFunctionExporter;
import com.tpo.labs.lab2.log.LnFunction;
import com.tpo.labs.lab2.log.LogBaseFunction;
import com.tpo.labs.lab2.trig.CosFunction;
import com.tpo.labs.lab2.trig.CotFunction;
import com.tpo.labs.lab2.trig.CscFunction;
import com.tpo.labs.lab2.trig.SecFunction;
import com.tpo.labs.lab2.trig.SinFunction;
import com.tpo.labs.lab2.trig.TanFunction;

import java.nio.file.Path;
import java.util.Locale;
import java.util.function.DoubleUnaryOperator;

public class Main {
    private static final String MODULE_NAME = "system";
    private static final double RANGE_START = -2.0;
    private static final double RANGE_END = 5.0;
    private static final double RANGE_STEP = 0.05;
    private static final Path OUTPUT_PATH = Path.of("system.csv");
    private static final String DELIMITER = ";";

    public static void main(String[] args) {
        String moduleName = MODULE_NAME.toLowerCase(Locale.ROOT);

        CsvFunctionExporter.exportRange(
                OUTPUT_PATH,
                moduleName,
                resolveModule(moduleName),
                RANGE_START,
                RANGE_END,
                RANGE_STEP,
                DELIMITER
        );
    }

    private static DoubleUnaryOperator resolveModule(String moduleName) {
        SinFunction sin = new SinFunction();
        CosFunction cos = new CosFunction(sin);
        LnFunction ln = new LnFunction();

        switch (moduleName) {
            case "system":
                return new SystemOfFunctions()::calculate;
            case "sin":
                return sin::calculate;
            case "cos":
                return cos::calculate;
            case "sec":
                return new SecFunction(cos)::calculate;
            case "csc":
                return new CscFunction(sin)::calculate;
            case "tan":
                return new TanFunction(sin, cos)::calculate;
            case "cot":
                return new CotFunction(sin, cos)::calculate;
            case "ln":
                return ln::calculate;
            case "log2":
                return new LogBaseFunction(ln, 2.0)::calculate;
            case "log3":
                return new LogBaseFunction(ln, 3.0)::calculate;
            case "log5":
                return new LogBaseFunction(ln, 5.0)::calculate;
            default:
                throw new IllegalArgumentException("Unknown module: " + moduleName);
        }
    }
}
