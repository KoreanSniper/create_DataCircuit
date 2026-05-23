package com.seoul2line.datacircuit.ponder;

import com.seoul2line.datacircuit.DataCircuitMod;
import net.createmod.ponder.foundation.PonderIndex;

public class DataCircuitPonderRegistration {
    private DataCircuitPonderRegistration() {
    }

    public static void register() {
        PonderIndex.addPlugin(new DataCircuitPonderPlugin());
    }
}
