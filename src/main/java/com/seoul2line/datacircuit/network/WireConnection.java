package com.seoul2line.datacircuit.network;

import net.minecraft.core.BlockPos;

public record WireConnection(BlockPos from, BlockPos to) {
    public WireConnection normalized() {
        return compare(from, to) <= 0 ? this : new WireConnection(to, from);
    }

    public boolean touches(BlockPos pos) {
        return from.equals(pos) || to.equals(pos);
    }

    private static int compare(BlockPos left, BlockPos right) {
        int x = Integer.compare(left.getX(), right.getX());
        if (x != 0) {
            return x;
        }
        int y = Integer.compare(left.getY(), right.getY());
        if (y != 0) {
            return y;
        }
        return Integer.compare(left.getZ(), right.getZ());
    }
}
