package com.tpo.labs.lab2.table;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

abstract class AbstractTableFunction {
    private final NavigableMap<Double, Double> values;
    private final double epsilon;

    protected AbstractTableFunction(Map<Double, Double> values, double epsilon) {
        this.values = new TreeMap<>(values);
        this.epsilon = epsilon;
    }

    protected static NavigableMap<Double, Double> load(Path path, String delimiter) {
        try {
            NavigableMap<Double, Double> values = new TreeMap<>();
            String separator = Pattern.quote(delimiter);

            for (String line : Files.readAllLines(path)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(separator, -1);
                if (parts.length < 2) {
                    continue;
                }

                try {
                    double x = Double.parseDouble(parts[0].trim());
                    double y = Double.parseDouble(parts[1].trim());
                    values.put(x, y);
                } catch (NumberFormatException ignored) {
                }
            }

            return values;
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    protected double calculateFromTable(double x) {
        Map.Entry<Double, Double> floor = values.floorEntry(x);
        Map.Entry<Double, Double> ceiling = values.ceilingEntry(x);

        Double result = match(floor, x);
        if (result != null) {
            return result;
        }

        result = match(ceiling, x);
        if (result != null) {
            return result;
        }

        throw new IllegalArgumentException("No tabular value for x=" + x);
    }

    private Double match(Map.Entry<Double, Double> entry, double x) {
        if (entry == null) {
            return null;
        }
        if (Math.abs(entry.getKey() - x) <= epsilon) {
            return entry.getValue();
        }
        return null;
    }
}
