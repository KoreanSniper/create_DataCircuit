package com.seoul2line.datacircuit.network;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class WireNetworkSavedData extends SavedData {
    private static final String DATA_NAME = "datacircuit_wire_network";
    private final Set<WireConnection> connections = new HashSet<>();

    public static WireNetworkSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(WireNetworkSavedData::load, WireNetworkSavedData::new, DATA_NAME);
    }

    public static WireNetworkSavedData load(CompoundTag tag) {
        WireNetworkSavedData data = new WireNetworkSavedData();
        ListTag list = tag.getList("connections", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            data.connections.add(new WireConnection(
                    BlockPos.of(entry.getLong("from")),
                    BlockPos.of(entry.getLong("to"))
            ).normalized());
        }
        return data;
    }

    public WireConnection addConnection(BlockPos from, BlockPos to) {
        WireConnection connection = new WireConnection(from.immutable(), to.immutable()).normalized();
        connections.add(connection);
        setDirty();
        return connection;
    }

    public Set<WireConnection> removeConnectionsAt(BlockPos pos) {
        Set<WireConnection> removed = new HashSet<>();
        connections.removeIf(connection -> {
            if (connection.touches(pos)) {
                removed.add(connection);
                return true;
            }
            return false;
        });
        if (!removed.isEmpty()) {
            setDirty();
        }
        return removed;
    }

    public boolean hasConnectionsAt(BlockPos pos) {
        return connections.stream().anyMatch(connection -> connection.touches(pos));
    }

    public Set<BlockPos> connectedTo(BlockPos pos) {
        Set<BlockPos> connected = new HashSet<>();
        for (WireConnection connection : connections) {
            if (connection.from().equals(pos)) {
                connected.add(connection.to());
            } else if (connection.to().equals(pos)) {
                connected.add(connection.from());
            }
        }
        return connected;
    }

    public Set<WireConnection> connections() {
        return Collections.unmodifiableSet(connections);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (WireConnection connection : connections) {
            CompoundTag entry = new CompoundTag();
            entry.putLong("from", connection.from().asLong());
            entry.putLong("to", connection.to().asLong());
            list.add(entry);
        }
        tag.put("connections", list);
        return tag;
    }
}
