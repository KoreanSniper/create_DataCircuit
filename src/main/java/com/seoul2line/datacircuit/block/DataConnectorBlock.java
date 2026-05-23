package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.network.WireNetworkSavedData;
import com.seoul2line.datacircuit.network.ModNetworking;
import com.seoul2line.datacircuit.network.WireNetworkUpdateNotifier;
import com.seoul2line.datacircuit.network.WireConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DataConnectorBlock extends Block {
    private static final Map<UUID, BlockPos> SELECTED_PORTS = new HashMap<>();

    public DataConnectorBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult handleConnectorUse(Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        if (serverPlayer.isShiftKeyDown()) {
            clearSelection(serverPlayer);
            ServerLevel serverLevel = (ServerLevel) level;
            Set<WireConnection> removed = WireNetworkSavedData.get(serverLevel).removeConnectionsAt(pos);
            ModNetworking.syncToAll(serverLevel);
            WireNetworkUpdateNotifier.notifyAroundChangedConnections(serverLevel, removed, pos);
            serverPlayer.displayClientMessage(Component.translatable("message.datacircuit.port_cleared").withStyle(ChatFormatting.RED), true);
            return InteractionResult.CONSUME;
        }

        BlockPos first = SELECTED_PORTS.remove(serverPlayer.getUUID());
        if (first == null) {
            SELECTED_PORTS.put(serverPlayer.getUUID(), pos.immutable());
            serverPlayer.displayClientMessage(Component.translatable("message.datacircuit.port_selected").withStyle(ChatFormatting.AQUA), true);
            return InteractionResult.CONSUME;
        }

        if (first.equals(pos)) {
            serverPlayer.displayClientMessage(Component.translatable("message.datacircuit.port_same").withStyle(ChatFormatting.YELLOW), true);
            return InteractionResult.CONSUME;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        WireConnection connection = WireNetworkSavedData.get(serverLevel).addConnection(first, pos);
        ModNetworking.syncToAll(serverLevel);
        WireNetworkUpdateNotifier.notifyAroundChangedConnection(serverLevel, connection);
        serverPlayer.displayClientMessage(Component.translatable("message.datacircuit.port_linked").withStyle(ChatFormatting.GREEN), true);
        return InteractionResult.CONSUME;
    }

    public static void clearSelection(ServerPlayer player) {
        SELECTED_PORTS.remove(player.getUUID());
    }

    public static boolean hasSelectedPort(ServerPlayer player) {
        return SELECTED_PORTS.containsKey(player.getUUID());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel) {
            Set<WireConnection> removed = WireNetworkSavedData.get(serverLevel).removeConnectionsAt(pos);
            ModNetworking.syncToAll(serverLevel);
            WireNetworkUpdateNotifier.notifyAroundChangedConnections(serverLevel, removed, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
