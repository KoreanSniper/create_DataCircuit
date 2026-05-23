package com.seoul2line.datacircuit.network;

import com.seoul2line.datacircuit.block.DataSignalReceiver;
import com.seoul2line.datacircuit.block.DataSignalSourceBlock;
import com.seoul2line.datacircuit.block.DataTypedReceiver;
import com.seoul2line.datacircuit.block.DataWireNodeBlock;
import com.seoul2line.datacircuit.block.LogicGateBlock;
import com.seoul2line.datacircuit.compat.CreateDisplayBridge;
import com.seoul2line.datacircuit.data.DataValue;
import com.seoul2line.datacircuit.data.DataValueType;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DataSignalPropagator {
    private static final int MAX_VISITED_PORTS = 256;

    private DataSignalPropagator() {
    }

    public static void propagateBoolean(ServerLevel level, BlockPos source, boolean powered) {
        propagateValue(level, source, DataValue.bit(powered));
    }

    public static void propagateValue(ServerLevel level, BlockPos source, DataValue value) {
        ModNetworking.sendPulse(level, source, value.visual());
        WireNetworkSavedData network = WireNetworkSavedData.get(level);
        Set<BlockPos> component = collectComponent(network, source);
        boolean powered = value.type() == DataValueType.BIT && value.truthy();
        boolean effectivePowered = powered || component.stream().anyMatch(pos -> isPoweredEmitter(level, pos));
        DataValue effectiveValue = DataValue.bit(effectivePowered);

        for (BlockPos pos : component) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof DataSignalReceiver receiver) {
                receiver.receiveDataValue(level, pos, effectiveValue);
            }
            if (block instanceof DataWireNodeBlock) {
                receiveThroughAttachedBlock(level, pos, state, value, effectiveValue);
            }
        }
    }

    private static Set<BlockPos> collectComponent(WireNetworkSavedData network, BlockPos source) {
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(source);

        while (!queue.isEmpty() && visited.size() < MAX_VISITED_PORTS) {
            BlockPos pos = queue.remove();
            if (!visited.add(pos)) {
                continue;
            }

            for (BlockPos next : network.connectedTo(pos)) {
                if (!visited.contains(next)) {
                    queue.add(next);
                }
            }
        }

        return visited;
    }

    private static boolean isPoweredEmitter(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof DataSignalSourceBlock && state.getValue(DataSignalSourceBlock.POWERED)) {
            return true;
        }
        if (state.getBlock() instanceof DataWireNodeBlock) {
            BlockPos attachedPos = pos.relative(state.getValue(DataWireNodeBlock.FACING).getOpposite());
            BlockState attachedState = level.getBlockState(attachedPos);
            if (attachedState.getBlock() instanceof LogicGateBlock
                    && attachedState.getValue(LogicGateBlock.FACING) == state.getValue(DataWireNodeBlock.FACING)
                    && attachedState.getValue(LogicGateBlock.POWERED)) {
                return true;
            }
        }
        return false;
    }

    private static void receiveThroughAttachedBlock(
            ServerLevel level,
            BlockPos nodePos,
            BlockState nodeState,
            DataValue sentValue,
            DataValue effectiveBooleanValue
    ) {
        BlockPos attachedPos = nodePos.relative(nodeState.getValue(DataWireNodeBlock.FACING).getOpposite());
        BlockState attachedState = level.getBlockState(attachedPos);
        if (CreateDisplayBridge.tryWrite(level, attachedPos, sentValue)) {
            return;
        }
        if (attachedState.getBlock() instanceof DataSignalReceiver receiver) {
            DataValue deliveredValue = attachedState.getBlock() instanceof DataTypedReceiver ? sentValue : effectiveBooleanValue;
            receiver.receiveDataValueFromNode(level, attachedPos, nodePos, deliveredValue);
        }
    }
}
