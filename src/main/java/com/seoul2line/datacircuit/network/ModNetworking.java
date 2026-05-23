package com.seoul2line.datacircuit.network;

import com.seoul2line.datacircuit.DataCircuitMod;
import com.seoul2line.datacircuit.data.DataPulseVisual;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DataCircuitMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId;

    private ModNetworking() {
    }

    public static void register() {
        CHANNEL.messageBuilder(SyncWireConnectionsPacket.class, packetId++)
                .encoder(SyncWireConnectionsPacket::encode)
                .decoder(SyncWireConnectionsPacket::decode)
                .consumerMainThread(SyncWireConnectionsPacket::handle)
                .add();
        CHANNEL.messageBuilder(DataPulsePacket.class, packetId++)
                .encoder(DataPulsePacket::encode)
                .decoder(DataPulsePacket::decode)
                .consumerMainThread(DataPulsePacket::handle)
                .add();
        CHANNEL.messageBuilder(SubmitTypedInputPacket.class, packetId++)
                .encoder(SubmitTypedInputPacket::encode)
                .decoder(SubmitTypedInputPacket::decode)
                .consumerMainThread(SubmitTypedInputPacket::handle)
                .add();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(ModNetworking::onPlayerLoggedIn);
    }

    public static void syncToAll(ServerLevel level) {
        CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), SyncWireConnectionsPacket.from(level));
    }

    public static void sendPulse(ServerLevel level, net.minecraft.core.BlockPos source, boolean powered) {
        sendPulse(level, source, DataPulseVisual.bit(powered));
    }

    public static void sendPulse(ServerLevel level, net.minecraft.core.BlockPos source, DataPulseVisual visual) {
        CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), new DataPulsePacket(source, visual));
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> (net.minecraft.server.level.ServerPlayer) event.getEntity()), SyncWireConnectionsPacket.from(serverLevel));
        }
    }
}
