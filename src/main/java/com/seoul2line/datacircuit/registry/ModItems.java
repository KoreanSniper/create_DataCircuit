package com.seoul2line.datacircuit.registry;

import com.seoul2line.datacircuit.DataCircuitMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DataCircuitMod.MOD_ID);

    public static final RegistryObject<Item> DATA_WIRE_NODE = ITEMS.register(
            "data_wire_node",
            () -> new BlockItem(ModBlocks.DATA_WIRE_NODE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> DATA_SIGNAL_SOURCE = ITEMS.register(
            "data_signal_source",
            () -> new BlockItem(ModBlocks.DATA_SIGNAL_SOURCE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> DATA_LAMP = ITEMS.register(
            "data_lamp",
            () -> new BlockItem(ModBlocks.DATA_LAMP.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> NUMBER_INPUT = ITEMS.register(
            "number_input",
            () -> new BlockItem(ModBlocks.NUMBER_INPUT.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> TEXT_INPUT = ITEMS.register(
            "text_input",
            () -> new BlockItem(ModBlocks.TEXT_INPUT.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> RGB_SELECTOR = ITEMS.register(
            "rgb_selector",
            () -> new BlockItem(ModBlocks.RGB_SELECTOR.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> ADD_BLOCK = ITEMS.register(
            "add_block",
            () -> new BlockItem(ModBlocks.ADD_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> SUBTRACT_BLOCK = ITEMS.register(
            "subtract_block",
            () -> new BlockItem(ModBlocks.SUBTRACT_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> MULTIPLY_BLOCK = ITEMS.register(
            "multiply_block",
            () -> new BlockItem(ModBlocks.MULTIPLY_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> DIVIDE_BLOCK = ITEMS.register(
            "divide_block",
            () -> new BlockItem(ModBlocks.DIVIDE_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> POWER_BLOCK = ITEMS.register(
            "power_block",
            () -> new BlockItem(ModBlocks.POWER_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> AND_GATE = ITEMS.register(
            "and_gate",
            () -> new BlockItem(ModBlocks.AND_GATE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> OR_GATE = ITEMS.register(
            "or_gate",
            () -> new BlockItem(ModBlocks.OR_GATE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> XOR_GATE = ITEMS.register(
            "xor_gate",
            () -> new BlockItem(ModBlocks.XOR_GATE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> NOT_GATE = ITEMS.register(
            "not_gate",
            () -> new BlockItem(ModBlocks.NOT_GATE.get(), new Item.Properties())
    );
}
