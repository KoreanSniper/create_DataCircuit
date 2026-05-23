package com.seoul2line.datacircuit.network;

import com.seoul2line.datacircuit.client.ClientWireNetwork;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

public record SyncWireConnectionsPacket(List<WireConnection> connections) {
    public static SyncWireConnectionsPacket from(ServerLevel level) {
        return new SyncWireConnectionsPacket(new ArrayList<>(WireNetworkSavedData.get(level).connections()));
    }

    public static void encode(SyncWireConnectionsPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.connections.size());
        for (WireConnection connection : packet.connections) {
            buffer.writeLong(connection.from().asLong());
            buffer.writeLong(connection.to().asLong());
        }
    }

    public static SyncWireConnectionsPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        List<WireConnection> connections = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            connections.add(new WireConnection(BlockPos.of(buffer.readLong()), BlockPos.of(buffer.readLong())));
        }
        return new SyncWireConnectionsPacket(connections);
    }

    public static void handle(SyncWireConnectionsPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ClientWireNetwork.setConnections(packet.connections));
        context.get().setPacketHandled(true);
    }
}
