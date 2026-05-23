package com.seoul2line.datacircuit;

import com.seoul2line.datacircuit.registry.ModBlocks;
import com.seoul2line.datacircuit.registry.ModBlockEntities;
import com.seoul2line.datacircuit.registry.ModCreativeTabs;
import com.seoul2line.datacircuit.registry.ModItems;
import com.seoul2line.datacircuit.event.DataCircuitEvents;
import com.seoul2line.datacircuit.network.ModNetworking;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DataCircuitMod.MOD_ID)
public class DataCircuitMod {
    public static final String MOD_ID = "datacircuit";

    public DataCircuitMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
        ModNetworking.register();
        MinecraftForge.EVENT_BUS.addListener(DataCircuitEvents::onBlockBreak);
        MinecraftForge.EVENT_BUS.addListener(DataCircuitEvents::onRightClickBlock);
        modBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        if (!ModList.get().isLoaded("ponder")) {
            return;
        }
        event.enqueueWork(() -> {
            try {
                Class.forName("com.seoul2line.datacircuit.ponder.DataCircuitPonderRegistration")
                        .getMethod("register")
                        .invoke(null);
            } catch (ReflectiveOperationException exception) {
                throw new IllegalStateException("Failed to register DataCircuit Ponder scenes", exception);
            }
        });
    }
}
