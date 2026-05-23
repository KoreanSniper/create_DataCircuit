package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.network.DataSignalPropagator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class LogicGateBlock extends HorizontalDirectionalBlock implements DataSignalReceiver {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty INPUT_A = BooleanProperty.create("input_a");
    public static final BooleanProperty INPUT_B = BooleanProperty.create("input_b");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private final LogicGateType type;

    public LogicGateBlock(LogicGateType type, Properties properties) {
        super(properties);
        this.type = type;
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(INPUT_A, false)
                .setValue(INPUT_B, false)
                .setValue(POWERED, evaluate(false, false)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(POWERED, evaluate(false, false));
    }

    @Override
    public void receiveDataSignal(Level level, BlockPos pos, boolean powered) {
        // Gates only accept signals through attached wire nodes so that input and output networks stay separate.
    }

    @Override
    public void receiveDataSignalFromNode(Level level, BlockPos pos, BlockPos nodePos, boolean powered) {
        if (level.isClientSide) {
            return;
        }

        BlockState state = level.getBlockState(pos);
        Direction side = directionFrom(pos, nodePos);
        if (side == null) {
            return;
        }
        Direction facing = state.getValue(FACING);
        Direction inputA = type == LogicGateType.NOT ? facing.getOpposite() : facing.getCounterClockWise();
        Direction inputB = facing.getClockWise();

        BlockState nextState = state;
        if (side == inputA) {
            nextState = nextState.setValue(INPUT_A, powered);
        } else if (type != LogicGateType.NOT && side == inputB) {
            nextState = nextState.setValue(INPUT_B, powered);
        } else {
            return;
        }

        boolean nextOutput = evaluate(nextState.getValue(INPUT_A), nextState.getValue(INPUT_B));
        nextState = nextState.setValue(POWERED, nextOutput);
        if (!nextState.equals(state)) {
            level.setBlock(pos, nextState, 3);
        }

        if (state.getValue(POWERED) != nextOutput) {
            propagateFromOutputNode((ServerLevel) level, pos, facing, nextOutput);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && !oldState.is(state.getBlock())) {
            propagateFromOutputNode((ServerLevel) level, pos, state.getValue(FACING), state.getValue(POWERED));
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide && fromPos.equals(pos.relative(state.getValue(FACING)))) {
            propagateFromOutputNode((ServerLevel) level, pos, state.getValue(FACING), state.getValue(POWERED));
        }
    }

    private boolean evaluate(boolean inputA, boolean inputB) {
        return switch (type) {
            case AND -> inputA && inputB;
            case OR -> inputA || inputB;
            case XOR -> inputA ^ inputB;
            case NOT -> !inputA;
        };
    }

    private void propagateFromOutputNode(ServerLevel level, BlockPos gatePos, Direction outputSide, boolean powered) {
        BlockPos outputNodePos = gatePos.relative(outputSide);
        BlockState outputNodeState = level.getBlockState(outputNodePos);
        if (outputNodeState.getBlock() instanceof DataWireNodeBlock
                && outputNodeState.getValue(DataWireNodeBlock.FACING) == outputSide) {
            DataSignalPropagator.propagateBoolean(level, outputNodePos, powered);
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
        builder.add(FACING, INPUT_A, INPUT_B, POWERED);
    }
}
