package com.seoul2line.datacircuit.network;

import com.seoul2line.datacircuit.block.DataTypedInputBlock;
import com.seoul2line.datacircuit.block.InputKind;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public record SubmitTypedInputPacket(BlockPos pos, InputKind kind, String input) {
    public static void encode(SubmitTypedInputPacket packet, FriendlyByteBuf buffer) {
        buffer.writeLong(packet.pos.asLong());
        buffer.writeEnum(packet.kind);
        buffer.writeUtf(packet.input, 64);
    }

    public static SubmitTypedInputPacket decode(FriendlyByteBuf buffer) {
        return new SubmitTypedInputPacket(
                BlockPos.of(buffer.readLong()),
                buffer.readEnum(InputKind.class),
                buffer.readUtf(64)
        );
    }

    public static void handle(SubmitTypedInputPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null && player.level() instanceof ServerLevel serverLevel) {
                DataTypedInputBlock.submitValue(serverLevel, player, packet.pos, packet.kind, packet.input);
            }
        });
        context.get().setPacketHandled(true);
    }
}
