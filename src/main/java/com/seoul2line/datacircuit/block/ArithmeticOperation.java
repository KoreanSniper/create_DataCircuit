package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.data.DataValue;
import com.seoul2line.datacircuit.data.DataValueConversions;
import com.seoul2line.datacircuit.data.DataValueType;

public enum ArithmeticOperation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    POWER;

    public DataValue apply(DataValue left, DataValue right) {
        if (left.type() == DataValueType.ERROR || right.type() == DataValueType.ERROR) {
            return DataValue.error();
        }
        if (left.type() == DataValueType.RGB || right.type() == DataValueType.RGB) {
            return applyRgb(left, right);
        }
        if (left.type() == DataValueType.TEXT || right.type() == DataValueType.TEXT) {
            return applyText(left, right);
        }

        double leftNumber = DataValueConversions.toNumber(left);
        double rightNumber = DataValueConversions.toNumber(right);
        return DataValue.number(applyNumber(leftNumber, rightNumber));
    }

    private double applyNumber(double left, double right) {
        return switch (this) {
            case ADD -> left + right;
            case SUBTRACT -> left - right;
            case MULTIPLY -> left * right;
            case DIVIDE -> right == 0.0D ? 0.0D : left / right;
            case POWER -> Math.pow(left, right);
        };
    }

    private DataValue applyText(DataValue left, DataValue right) {
        String leftText = toText(left);
        String rightText = toText(right);
        return switch (this) {
            case ADD -> DataValue.text(leftText + " " + rightText);
            case SUBTRACT -> DataValue.text(removeFirst(leftText, rightText));
            case MULTIPLY -> multiplyText(left, right, leftText, rightText);
            case DIVIDE -> DataValue.text(rightText.isEmpty() ? leftText : leftText.replace(rightText, ""));
            case POWER -> DataValue.error();
        };
    }

    private DataValue multiplyText(DataValue left, DataValue right, String leftText, String rightText) {
        if (left.type() == DataValueType.TEXT && right.type() == DataValueType.TEXT) {
            return DataValue.error();
        }
        if (left.type() == DataValueType.TEXT) {
            return DataValue.text(repeat(leftText, DataValueConversions.toNumber(right)));
        }
        return DataValue.text(repeat(rightText, DataValueConversions.toNumber(left)));
    }

    private DataValue applyRgb(DataValue left, DataValue right) {
        if (this == ADD) {
            return DataValue.rgb(mixRgb(toRgb(left), toRgb(right)));
        }
        if (this == SUBTRACT) {
            return DataValue.rgb(subtractRgb(toRgb(left), toRgb(right)));
        }
        return DataValue.error();
    }

    private static String toText(DataValue value) {
        return value.label();
    }

    private static String removeFirst(String text, String target) {
        if (target.isEmpty()) {
            return text;
        }
        int index = text.indexOf(target);
        if (index < 0) {
            return text;
        }
        return text.substring(0, index) + text.substring(index + target.length());
    }

    private static String repeat(String text, double amount) {
        int count = Math.max(0, Math.min(64, (int) Math.floor(amount)));
        return text.repeat(count);
    }

    private static int toRgb(DataValue value) {
        if (value.type() == DataValueType.RGB) {
            return value.color() & 0xFFFFFF;
        }
        int channel = clamp((int) DataValueConversions.toNumber(value));
        return (channel << 16) | (channel << 8) | channel;
    }

    private static int mixRgb(int left, int right) {
        int r = (((left >> 16) & 0xFF) + ((right >> 16) & 0xFF)) / 2;
        int g = (((left >> 8) & 0xFF) + ((right >> 8) & 0xFF)) / 2;
        int b = ((left & 0xFF) + (right & 0xFF)) / 2;
        return (r << 16) | (g << 8) | b;
    }

    private static int subtractRgb(int left, int right) {
        int r = Math.max(0, ((left >> 16) & 0xFF) - ((right >> 16) & 0xFF));
        int g = Math.max(0, ((left >> 8) & 0xFF) - ((right >> 8) & 0xFF));
        int b = Math.max(0, (left & 0xFF) - (right & 0xFF));
        return (r << 16) | (g << 8) | b;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
