package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.data.DataValue;
import com.seoul2line.datacircuit.data.DataValueType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface DataSignalReceiver {
    void receiveDataSignal(Level level, BlockPos pos, boolean powered);

    default void receiveDataValue(Level level, BlockPos pos, DataValue value) {
        receiveDataSignal(level, pos, value.type() == DataValueType.BIT && value.truthy());
    }

    default void receiveDataSignalFromNode(Level level, BlockPos pos, BlockPos nodePos, boolean powered) {
        receiveDataSignal(level, pos, powered);
    }

    default void receiveDataValueFromNode(Level level, BlockPos pos, BlockPos nodePos, DataValue value) {
        receiveDataValue(level, pos, value);
    }
}
