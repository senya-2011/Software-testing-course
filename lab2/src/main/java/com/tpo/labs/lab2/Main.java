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
    public static void main(String[] args) {
        if (args.length < 5 || args.length > 6) {
            printUsage();
            return;
        }

        String moduleName = args[0].toLowerCase(Locale.ROOT);
        double start = Double.parseDouble(args[1]);
        double end = Double.parseDouble(args[2]);
        double step = Double.parseDouble(args[3]);
        Path outputPath = Path.of(args[4]);
        String delimiter = args.length == 6 ? args[5] : ";";

        CsvFunctionExporter.exportRange(
                outputPath,
                moduleName,
                resolveModule(moduleName),
                start,
                end,
                step,
                delimiter
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

    private static void printUsage() {
        System.out.println("Usage: <module> <start> <end> <step> <output.csv> [delimiter]");
        System.out.println("Modules: system, sin, cos, sec, csc, tan, cot, ln, log2, log3, log5");
    }
}
