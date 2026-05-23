package com.seoul2line.datacircuit.client;

import com.seoul2line.datacircuit.block.InputKind;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DataCircuitClientScreens {
    private DataCircuitClientScreens() {
    }

    public static void openTypedInput(BlockPos pos, InputKind kind) {
        Minecraft.getInstance().setScreen(new DataTypedInputScreen(pos, kind));
    }
}
