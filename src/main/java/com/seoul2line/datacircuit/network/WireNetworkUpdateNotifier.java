package com.seoul2line.datacircuit.network;

import com.seoul2line.datacircuit.block.DataWireNodeBlock;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class WireNetworkUpdateNotifier {
    private static final int MAX_WIRE_DISTANCE = 10;

    private WireNetworkUpdateNotifier() {
    }

    public static void notifyAroundChangedConnection(ServerLevel level, WireConnection connection) {
        Set<BlockPos> seeds = new HashSet<>();
        seeds.add(connection.from());
        seeds.add(connection.to());
        notifyAroundSeeds(level, seeds);
    }

    public static void notifyAroundChangedConnections(ServerLevel level, Set<WireConnection> connections, BlockPos fallbackSeed) {
        Set<BlockPos> seeds = new HashSet<>();
        seeds.add(fallbackSeed);
        for (WireConnection connection : connections) {
            seeds.add(connection.from());
            seeds.add(connection.to());
        }
        notifyAroundSeeds(level, seeds);
    }

    private static void notifyAroundSeeds(ServerLevel level, Set<BlockPos> seeds) {
        WireNetworkSavedData network = WireNetworkSavedData.get(level);
        Queue<NodeDistance> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        for (BlockPos seed : seeds) {
            queue.add(new NodeDistance(seed, 0));
        }

        while (!queue.isEmpty()) {
            NodeDistance current = queue.remove();
            if (current.distance() > MAX_WIRE_DISTANCE || !visited.add(current.pos())) {
                continue;
            }

            notifyBlock(level, current.pos());
            notifyAttachedBlock(level, current.pos());

            for (BlockPos next : network.connectedTo(current.pos())) {
                if (!visited.contains(next)) {
                    queue.add(new NodeDistance(next, current.distance() + 1));
                }
            }
        }
    }

    private static void notifyAttachedBlock(ServerLevel level, BlockPos nodePos) {
        BlockState state = level.getBlockState(nodePos);
        if (state.getBlock() instanceof DataWireNodeBlock) {
            notifyBlock(level, nodePos.relative(state.getValue(DataWireNodeBlock.FACING).getOpposite()));
        }
    }

    private static void notifyBlock(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return;
        }

        Block block = state.getBlock();
        level.blockUpdated(pos, block);
        level.updateNeighborsAt(pos, block);
    }

    private record NodeDistance(BlockPos pos, int distance) {
    }
}
