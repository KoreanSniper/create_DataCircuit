package com.seoul2line.datacircuit.data;

import net.minecraft.nbt.CompoundTag;

public record DataValue(DataValueType type, String label, int color, boolean rgbPreview, boolean truthy) {
    public static DataValue bit(boolean powered) {
        return new DataValue(DataValueType.BIT, powered ? "1" : "0", powered ? 0x55FFFF : 0x66AACC, false, powered);
    }

    public static DataValue number(int number) {
        return number((double) number);
    }

    public static DataValue number(double number) {
        String label = Math.rint(number) == number
                ? Long.toString((long) number)
                : String.format(java.util.Locale.ROOT, "%.4f", number).replaceAll("0+$", "").replaceAll("\\.$", "");
        return new DataValue(DataValueType.NUMBER, label, 0xFFD166, false, number != 0.0D);
    }

    public static DataValue text(String text) {
        return new DataValue(DataValueType.TEXT, limit(text), 0xEAEAEA, false, !text.isEmpty());
    }

    public static DataValue hex(int value) {
        return new DataValue(DataValueType.HEX, String.format("0x%X", value), 0xB388FF, false, value != 0);
    }

    public static DataValue rgb(int rgb) {
        int color = rgb & 0xFFFFFF;
        return new DataValue(DataValueType.RGB, String.format("#%06X", color), color, true, color != 0);
    }

    public static DataValue error() {
        return new DataValue(DataValueType.ERROR, "!", 0xFF3333, false, false);
    }

    public DataPulseVisual visual() {
        return new DataPulseVisual(limit(label), color, rgbPreview);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Type", type.name());
        tag.putString("Label", label);
        tag.putInt("Color", color);
        tag.putBoolean("RgbPreview", rgbPreview);
        tag.putBoolean("Truthy", truthy);
        return tag;
    }

    public static DataValue load(CompoundTag tag) {
        DataValueType type;
        try {
            type = DataValueType.valueOf(tag.getString("Type"));
        } catch (IllegalArgumentException ignored) {
            type = DataValueType.NUMBER;
        }
        return new DataValue(
                type,
                tag.getString("Label"),
                tag.getInt("Color"),
                tag.getBoolean("RgbPreview"),
                tag.getBoolean("Truthy")
        );
    }

    private static String limit(String text) {
        if (text.length() <= 12) {
            return text;
        }
        return text.substring(0, 11) + ".";
    }
}
