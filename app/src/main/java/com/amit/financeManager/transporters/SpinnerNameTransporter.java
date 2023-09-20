package com.amit.financeManager.transporters;

public class SpinnerNameTransporter {
    private static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        SpinnerNameTransporter.name = name;
    }
}
