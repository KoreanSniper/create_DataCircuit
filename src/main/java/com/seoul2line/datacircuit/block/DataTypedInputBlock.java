package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.data.DataValue;
import com.seoul2line.datacircuit.client.DataCircuitClientScreens;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class DataTypedInputBlock extends DataConnectorBlock {
    private final InputKind kind;

    public DataTypedInputBlock(InputKind kind, Properties properties) {
        super(properties);
        this.kind = kind;
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
            if (!player.isShiftKeyDown()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DataCircuitClientScreens.openTypedInput(pos, kind));
            }
            return InteractionResult.SUCCESS;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        ServerPlayer serverPlayer = (ServerPlayer) player;
        WireNetworkSavedData network = WireNetworkSavedData.get(serverLevel);
        if (!serverPlayer.isShiftKeyDown() && !hasSelectedPort(serverPlayer) && network.hasConnectionsAt(pos)) {
            return InteractionResult.CONSUME;
        }

        return handleConnectorUse(level, pos, player, hand);
    }

    public InputKind kind() {
        return kind;
    }

    public static void submitValue(ServerLevel level, ServerPlayer player, BlockPos pos, InputKind kind, String rawInput) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof DataTypedInputBlock inputBlock) || inputBlock.kind() != kind) {
            return;
        }

        DataValue value = kind.parse(rawInput);
        DataSignalPropagator.propagateValue(level, pos, value);
        player.displayClientMessage(Component.translatable(
                "message.datacircuit.source_value",
                value.type().name(),
                value.label()
        ).withStyle(ChatFormatting.AQUA), true);
    }
}
