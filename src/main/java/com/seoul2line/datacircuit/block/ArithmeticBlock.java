package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.data.DataValue;
import com.seoul2line.datacircuit.network.DataSignalPropagator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class ArithmeticBlock extends HorizontalDirectionalBlock implements DataSignalReceiver, DataTypedReceiver, EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private final ArithmeticOperation operation;

    public ArithmeticBlock(ArithmeticOperation operation, Properties properties) {
        super(properties);
        this.operation = operation;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void receiveDataSignal(Level level, BlockPos pos, boolean powered) {
        // Arithmetic blocks only accept data through attached wire nodes.
    }

    @Override
    public void receiveDataValueFromNode(Level level, BlockPos pos, BlockPos nodePos, DataValue value) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockState state = level.getBlockState(pos);
        Direction side = directionFrom(pos, nodePos);
        if (side == null) {
            return;
        }

        Direction facing = state.getValue(FACING);
        Direction inputA = facing.getCounterClockWise();
        Direction inputB = facing.getClockWise();
        if (!(level.getBlockEntity(pos) instanceof ArithmeticBlockEntity arithmetic)) {
            return;
        }

        if (side == inputA) {
            arithmetic.setInputA(value);
        } else if (side == inputB) {
            arithmetic.setInputB(value);
        } else {
            return;
        }

        propagateResult(serverLevel, pos, facing, operation.apply(arithmetic.inputA(), arithmetic.inputB()));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArithmeticBlockEntity(pos, state);
    }

    private static void propagateResult(ServerLevel level, BlockPos pos, Direction outputSide, DataValue result) {
        BlockPos outputNodePos = pos.relative(outputSide);
        BlockState outputNodeState = level.getBlockState(outputNodePos);
        if (outputNodeState.getBlock() instanceof DataWireNodeBlock
                && outputNodeState.getValue(DataWireNodeBlock.FACING) == outputSide) {
            DataSignalPropagator.propagateValue(level, outputNodePos, result);
        }
    }

    private static Direction directionFrom(BlockPos from, BlockPos to) {
        int dx = Integer.compare(to.getX() - from.getX(), 0);
        int dy = Integer.compare(to.getY() - from.getY(), 0);
        int dz = Integer.compare(to.getZ() - from.getZ(), 0);
        return Direction.fromDelta(dx, dy, dz);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
