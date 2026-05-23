package com.seoul2line.datacircuit.network;

import com.seoul2line.datacircuit.client.ClientWireNetwork;
import com.seoul2line.datacircuit.data.DataPulseVisual;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record DataPulsePacket(BlockPos source, DataPulseVisual visual) {
    public static void encode(DataPulsePacket packet, FriendlyByteBuf buffer) {
        buffer.writeLong(packet.source.asLong());
        buffer.writeUtf(packet.visual.label(), 64);
        buffer.writeInt(packet.visual.color());
        buffer.writeBoolean(packet.visual.rgbPreview());
    }

    public static DataPulsePacket decode(FriendlyByteBuf buffer) {
        return new DataPulsePacket(
                BlockPos.of(buffer.readLong()),
                new DataPulseVisual(buffer.readUtf(64), buffer.readInt(), buffer.readBoolean())
        );
    }

    public static void handle(DataPulsePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ClientWireNetwork.addPulse(packet.source, packet.visual));
        context.get().setPacketHandled(true);
    }
}
