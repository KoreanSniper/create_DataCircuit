package com.seoul2line.datacircuit.compat;

import com.seoul2line.datacircuit.data.DataValue;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CreateDisplayBridge {
    private CreateDisplayBridge() {
    }

    public static boolean tryWrite(ServerLevel level, BlockPos pos, DataValue value) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof NixieTubeBlockEntity nixieTube) {
            writeNixie(level, nixieTube, value);
            return true;
        }
        if (blockEntity instanceof FlapDisplayBlockEntity flapDisplay) {
            writeDisplayBoard(level, flapDisplay, value);
            return true;
        }
        return false;
    }

    private static void writeNixie(ServerLevel level, NixieTubeBlockEntity nixieTube, DataValue value) {
        String text = value.label();
        if (text.isEmpty()) {
            nixieTube.displayEmptyText(value.color());
        } else {
            nixieTube.displayCustomText(text, value.color());
        }
        refresh(level, nixieTube);
    }

    private static void writeDisplayBoard(ServerLevel level, FlapDisplayBlockEntity flapDisplay, DataValue value) {
        FlapDisplayBlockEntity controller = flapDisplay.getController();
        FlapDisplayBlockEntity target = controller == null ? flapDisplay : controller;
        String text = value.label();
        int maxLength = Math.max(1, target.getMaxCharCount(0));
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength);
        }
        target.applyTextManually(0, text);
        refresh(level, target);
    }

    private static void refresh(ServerLevel level, BlockEntity blockEntity) {
        blockEntity.setChanged();
        level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
    }
}
