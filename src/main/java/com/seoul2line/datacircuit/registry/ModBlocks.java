package com.seoul2line.datacircuit.registry;

import com.seoul2line.datacircuit.DataCircuitMod;
import com.seoul2line.datacircuit.block.ArithmeticBlock;
import com.seoul2line.datacircuit.block.ArithmeticOperation;
import com.seoul2line.datacircuit.block.DataLampBlock;
import com.seoul2line.datacircuit.block.DataSignalSourceBlock;
import com.seoul2line.datacircuit.block.DataTypedInputBlock;
import com.seoul2line.datacircuit.block.DataWireNodeBlock;
import com.seoul2line.datacircuit.block.InputKind;
import com.seoul2line.datacircuit.block.LogicGateBlock;
import com.seoul2line.datacircuit.block.LogicGateType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DataCircuitMod.MOD_ID);

    public static final RegistryObject<Block> DATA_WIRE_NODE = BLOCKS.register(
            "data_wire_node",
            () -> new DataWireNodeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops())
    );

    public static final RegistryObject<Block> DATA_SIGNAL_SOURCE = BLOCKS.register(
            "data_signal_source",
            () -> new DataSignalSourceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops())
    );

    public static final RegistryObject<Block> DATA_LAMP = BLOCKS.register(
            "data_lamp",
            () -> new DataLampBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(1.0F, 3.0F)
                    .sound(SoundType.GLASS)
                    .lightLevel(state -> state.getValue(DataLampBlock.LIT) ? 15 : 0))
    );

    public static final RegistryObject<Block> NUMBER_INPUT = registerTypedInput("number_input", InputKind.NUMBER, MapColor.COLOR_YELLOW);
    public static final RegistryObject<Block> TEXT_INPUT = registerTypedInput("text_input", InputKind.TEXT, MapColor.COLOR_LIGHT_GRAY);
    public static final RegistryObject<Block> RGB_SELECTOR = registerTypedInput("rgb_selector", InputKind.RGB, MapColor.COLOR_MAGENTA);

    public static final RegistryObject<Block> ADD_BLOCK = registerArithmetic("add_block", ArithmeticOperation.ADD);
    public static final RegistryObject<Block> SUBTRACT_BLOCK = registerArithmetic("subtract_block", ArithmeticOperation.SUBTRACT);
    public static final RegistryObject<Block> MULTIPLY_BLOCK = registerArithmetic("multiply_block", ArithmeticOperation.MULTIPLY);
    public static final RegistryObject<Block> DIVIDE_BLOCK = registerArithmetic("divide_block", ArithmeticOperation.DIVIDE);
    public static final RegistryObject<Block> POWER_BLOCK = registerArithmetic("power_block", ArithmeticOperation.POWER);

    public static final RegistryObject<Block> AND_GATE = registerGate("and_gate", LogicGateType.AND);
    public static final RegistryObject<Block> OR_GATE = registerGate("or_gate", LogicGateType.OR);
    public static final RegistryObject<Block> XOR_GATE = registerGate("xor_gate", LogicGateType.XOR);
    public static final RegistryObject<Block> NOT_GATE = registerGate("not_gate", LogicGateType.NOT);

    private static RegistryObject<Block> registerGate(String name, LogicGateType type) {
        return BLOCKS.register(
                name,
                () -> new LogicGateBlock(type, BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BLUE)
                        .strength(2.0F, 6.0F)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops())
        );
    }

    private static RegistryObject<Block> registerTypedInput(String name, InputKind kind, MapColor color) {
        return BLOCKS.register(
                name,
                () -> new DataTypedInputBlock(kind, BlockBehaviour.Properties.of()
                        .mapColor(color)
                        .strength(2.0F, 6.0F)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops())
        );
    }

    private static RegistryObject<Block> registerArithmetic(String name, ArithmeticOperation operation) {
        return BLOCKS.register(
                name,
                () -> new ArithmeticBlock(operation, BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_PURPLE)
                        .strength(2.0F, 6.0F)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops())
        );
    }
}
