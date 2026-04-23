package com.tpo.labs.lab2.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public final class CsvFunctionExporter {
    private static final double LOOP_EPS = 1e-12;

    private CsvFunctionExporter() {
    }

    public static void exportRange(
            Path path,
            String moduleName,
            DoubleUnaryOperator function,
            double start,
            double end,
            double step,
            String delimiter
    ) {
        validateStep(start, end, step);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writeHeader(writer, moduleName, delimiter);

            int index = 0;
            double x = start;
            while (isWithinRange(x, end, step)) {
                writeRow(writer, x, function.applyAsDouble(x), delimiter);
                index++;
                x = start + (step * index);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static void exportPoints(
            Path path,
            String moduleName,
            DoubleUnaryOperator function,
            List<Double> points,
            String delimiter
    ) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writeHeader(writer, moduleName, delimiter);
            for (double x : points) {
                writeRow(writer, x, function.applyAsDouble(x), delimiter);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static void validateStep(double start, double end, double step) {
        if (step == 0.0) {
            throw new IllegalArgumentException("Step must not be zero");
        }
        if (start < end && step < 0.0) {
            throw new IllegalArgumentException("Step must be positive for an ascending range");
        }
        if (start > end && step > 0.0) {
            throw new IllegalArgumentException("Step must be negative for a descending range");
        }
    }

    private static boolean isWithinRange(double x, double end, double step) {
        return step > 0.0 ? x <= end + LOOP_EPS : x >= end - LOOP_EPS;
    }

    private static void writeHeader(BufferedWriter writer, String moduleName, String delimiter) throws IOException {
        writer.write("X");
        writer.write(delimiter);
        writer.write(moduleName);
        writer.write("(X)");
        writer.newLine();
    }

    private static void writeRow(BufferedWriter writer, double x, double y, String delimiter) throws IOException {
        writer.write(Double.toString(x));
        writer.write(delimiter);
        writer.write(Double.toString(y));
        writer.newLine();
    }
}
