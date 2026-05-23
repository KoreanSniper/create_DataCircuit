package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.data.DataValue;

public enum InputKind {
    NUMBER,
    TEXT,
    RGB;

    public String defaultInput() {
        return switch (this) {
            case NUMBER -> "42";
            case TEXT -> "DATA";
            case RGB -> "#33AAFF";
        };
    }

    public String titleKey() {
        return switch (this) {
            case NUMBER -> "screen.datacircuit.number_input";
            case TEXT -> "screen.datacircuit.text_input";
            case RGB -> "screen.datacircuit.rgb_selector";
        };
    }

    public DataValue parse(String rawInput) {
        String input = rawInput.trim();
        return switch (this) {
            case NUMBER -> DataValue.number(parseNumber(input));
            case TEXT -> DataValue.text(input);
            case RGB -> DataValue.rgb(parseRgb(input));
        };
    }

    private static int parseNumber(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static int parseRgb(String input) {
        String normalized = input;
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        } else if (normalized.startsWith("0x") || normalized.startsWith("0X")) {
            normalized = normalized.substring(2);
        }
        try {
            return Integer.parseInt(normalized, 16) & 0xFFFFFF;
        } catch (NumberFormatException ignored) {
            return 0xFFFFFF;
        }
    }
}
