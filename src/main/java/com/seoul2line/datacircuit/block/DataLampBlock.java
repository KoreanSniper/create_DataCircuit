package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.data.DataValue;
import com.seoul2line.datacircuit.data.DataValueType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class DataLampBlock extends DataConnectorBlock implements DataSignalReceiver {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public DataLampBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        return handleConnectorUse(level, pos, player, hand);
    }

    @Override
    public void receiveDataSignal(Level level, BlockPos pos, boolean powered) {
        BlockState state = level.getBlockState(pos);
        if (state.getValue(LIT) != powered) {
            level.setBlock(pos, state.setValue(LIT, powered), 3);
        }
    }

    @Override
    public void receiveDataValue(Level level, BlockPos pos, DataValue value) {
        receiveDataSignal(level, pos, value.type() == DataValueType.BIT && value.truthy());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(LIT);
    }
}
