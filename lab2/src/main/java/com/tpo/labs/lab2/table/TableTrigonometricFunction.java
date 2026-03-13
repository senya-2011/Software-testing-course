package com.tpo.labs.lab2.table;

import com.tpo.labs.lab2.trig.TrigonometricFunction;

import java.nio.file.Path;
import java.util.Map;

public class TableTrigonometricFunction extends AbstractTableFunction implements TrigonometricFunction {
    public TableTrigonometricFunction(Map<Double, Double> values, double epsilon) {
        super(values, epsilon);
    }

    public static TableTrigonometricFunction fromCsv(Path path, String delimiter, double epsilon) {
        return new TableTrigonometricFunction(load(path, delimiter), epsilon);
    }

    @Override
    public double calculate(double x) {
        return calculateFromTable(x);
    }
}
