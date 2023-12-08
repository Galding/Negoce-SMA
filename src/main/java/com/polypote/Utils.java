package com.polypote;

public class Utils {
    public static double addAPercentageToThePrice(double price, double x) {
        return Math.round(price * x * 100d) / 100d;
    }
}
