package com.seoul2line.datacircuit.client;

import com.seoul2line.datacircuit.data.DataPulseVisual;
import com.seoul2line.datacircuit.network.WireConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;

public class ClientWireNetwork {
    private static final List<WireConnection> CONNECTIONS = new ArrayList<>();
    private static final List<Pulse> PULSES = new ArrayList<>();

    private ClientWireNetwork() {
    }

    public static void setConnections(List<WireConnection> connections) {
        CONNECTIONS.clear();
        CONNECTIONS.addAll(connections);
    }

    public static List<WireConnection> connections() {
        return Collections.unmodifiableList(CONNECTIONS);
    }

    public static void addPulse(BlockPos source, DataPulseVisual visual) {
        PULSES.add(new Pulse(source, visual, System.currentTimeMillis()));
    }

    public static List<Pulse> pulses() {
        PULSES.removeIf(pulse -> System.currentTimeMillis() - pulse.startedAtMillis > 1100L);
        return Collections.unmodifiableList(PULSES);
    }

    public record Pulse(BlockPos source, DataPulseVisual visual, long startedAtMillis) {
    }
}
