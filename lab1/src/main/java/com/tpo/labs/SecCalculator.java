package com.tpo.labs;

public class SecCalculator {

    private static final double EPS = 1e-6;

    public static double sec(double x){
        double result = 0.0;
        double term;
        int n = 0;

        do{
            term = calculateTerm(x, n);
            result += term;
            n++;
        }while(Math.abs(term) > EPS);

        return result;
    }

    private static double calculateTerm(double x, int n){
        switch (n) {
            case 0:
                return 1;
            case 1:
                return (x * x) / 2.0;
            case 2:
                return (5 * Math.pow(x, 4)) / 24.0;
            case 3:
                return (61 * Math.pow(x, 6)) / 720.0;
            case 4:
                return (277 * Math.pow(x, 8)) / 8064.0;
            default:
                return 0;
        }
    }
}
