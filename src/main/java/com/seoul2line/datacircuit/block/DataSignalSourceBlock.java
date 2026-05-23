package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.network.DataSignalPropagator;
import com.seoul2line.datacircuit.network.WireNetworkSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class DataSignalSourceBlock extends DataConnectorBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public DataSignalSourceBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false));
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
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        ServerPlayer serverPlayer = (ServerPlayer) player;
        WireNetworkSavedData network = WireNetworkSavedData.get(serverLevel);

        if (!serverPlayer.isShiftKeyDown() && !hasSelectedPort(serverPlayer) && network.hasConnectionsAt(pos)) {
            boolean nextPower = !state.getValue(POWERED);
            level.setBlock(pos, state.setValue(POWERED, nextPower), 3);
            DataSignalPropagator.propagateBoolean(serverLevel, pos, nextPower);
            serverPlayer.displayClientMessage(Component.translatable(
                    nextPower ? "message.datacircuit.source_on" : "message.datacircuit.source_off"
            ).withStyle(nextPower ? ChatFormatting.GREEN : ChatFormatting.GRAY), true);
            return InteractionResult.CONSUME;
        }

        return handleConnectorUse(level, pos, player, hand);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(POWERED);
    }
}
