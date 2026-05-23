package com.seoul2line.datacircuit.registry;

import com.seoul2line.datacircuit.DataCircuitMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DataCircuitMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DATA_CIRCUIT_TAB = TABS.register(
            "data_circuit",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.datacircuit"))
                    .icon(() -> ModItems.DATA_WIRE_NODE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.DATA_WIRE_NODE.get());
                        output.accept(ModItems.DATA_SIGNAL_SOURCE.get());
                        output.accept(ModItems.DATA_LAMP.get());
                        output.accept(ModItems.NUMBER_INPUT.get());
                        output.accept(ModItems.TEXT_INPUT.get());
                        output.accept(ModItems.RGB_SELECTOR.get());
                        output.accept(ModItems.ADD_BLOCK.get());
                        output.accept(ModItems.SUBTRACT_BLOCK.get());
                        output.accept(ModItems.MULTIPLY_BLOCK.get());
                        output.accept(ModItems.DIVIDE_BLOCK.get());
                        output.accept(ModItems.POWER_BLOCK.get());
                        output.accept(ModItems.AND_GATE.get());
                        output.accept(ModItems.OR_GATE.get());
                        output.accept(ModItems.XOR_GATE.get());
                        output.accept(ModItems.NOT_GATE.get());
                    })
                    .build()
    );
}
