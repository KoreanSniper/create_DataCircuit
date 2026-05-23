package com.seoul2line.datacircuit.event;

import com.seoul2line.datacircuit.block.DataConnectorBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class DataCircuitEvents {
    private DataCircuitEvents() {
    }

    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player && DataConnectorBlock.hasSelectedPort(player)) {
            DataConnectorBlock.clearSelection(player);
            player.displayClientMessage(Component.translatable("message.datacircuit.selection_cancelled_break").withStyle(ChatFormatting.YELLOW), true);
        }
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || event.getHand() != InteractionHand.MAIN_HAND
                || !player.isShiftKeyDown()
                || !DataConnectorBlock.hasSelectedPort(player)) {
            return;
        }

        if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof DataConnectorBlock) {
            return;
        }

        DataConnectorBlock.clearSelection(player);
        player.displayClientMessage(Component.translatable("message.datacircuit.selection_cancelled").withStyle(ChatFormatting.YELLOW), true);
        event.setCanceled(true);
        event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
    }
}
