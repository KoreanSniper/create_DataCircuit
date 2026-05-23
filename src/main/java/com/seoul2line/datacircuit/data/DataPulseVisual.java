package com.seoul2line.datacircuit.data;

public record DataPulseVisual(String label, int color, boolean rgbPreview) {
    public static DataPulseVisual bit(boolean powered) {
        return DataValue.bit(powered).visual();
    }

    public static DataPulseVisual number(String number) {
        return new DataPulseVisual(limit(number), 0xFFD166, false);
    }

    public static DataPulseVisual text(String text) {
        return new DataPulseVisual(limit(text), 0xEAEAEA, false);
    }

    public static DataPulseVisual hex(String hex) {
        return new DataPulseVisual(hex, 0xB388FF, false);
    }

    public static DataPulseVisual rgb(int rgb) {
        return new DataPulseVisual(String.format("#%06X", rgb & 0xFFFFFF), rgb & 0xFFFFFF, true);
    }

    private static String limit(String text) {
        if (text.length() <= 12) {
            return text;
        }
        return text.substring(0, 11) + ".";
    }
}
