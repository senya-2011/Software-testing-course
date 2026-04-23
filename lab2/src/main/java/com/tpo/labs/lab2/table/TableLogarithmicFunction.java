package com.tpo.labs.lab2.table;

import com.tpo.labs.lab2.log.LogarithmicFunction;

import java.nio.file.Path;
import java.util.Map;

public class TableLogarithmicFunction extends AbstractTableFunction implements LogarithmicFunction {
    public TableLogarithmicFunction(Map<Double, Double> values, double epsilon) {
        super(values, epsilon);
    }

    public static TableLogarithmicFunction fromCsv(Path path, String delimiter, double epsilon) {
        return new TableLogarithmicFunction(load(path, delimiter), epsilon);
    }

    @Override
    public double calculate(double x) {
        return calculateFromTable(x);
    }
}
