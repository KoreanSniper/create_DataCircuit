package com.seoul2line.datacircuit.data;

public class DataValueConversions {
    private DataValueConversions() {
    }

    public static double toNumber(DataValue value) {
        return switch (value.type()) {
            case BIT -> value.truthy() ? 1.0D : 0.0D;
            case NUMBER -> parseDecimal(value.label());
            case TEXT -> parseDecimal(value.label());
            case HEX -> parseHex(value.label());
            case RGB -> value.color() & 0xFFFFFF;
            case ERROR -> 0.0D;
        };
    }

    private static double parseDecimal(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException ignored) {
            return 0.0D;
        }
    }

    private static double parseHex(String text) {
        String normalized = text.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        } else if (normalized.startsWith("0x") || normalized.startsWith("0X")) {
            normalized = normalized.substring(2);
        }
        try {
            return Integer.parseInt(normalized, 16);
        } catch (NumberFormatException ignored) {
            return 0.0D;
        }
    }
}
