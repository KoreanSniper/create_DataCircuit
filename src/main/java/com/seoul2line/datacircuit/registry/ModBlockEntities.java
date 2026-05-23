package com.seoul2line.datacircuit.registry;

import com.seoul2line.datacircuit.DataCircuitMod;
import com.seoul2line.datacircuit.block.ArithmeticBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DataCircuitMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ArithmeticBlockEntity>> ARITHMETIC_BLOCK = BLOCK_ENTITIES.register(
            "arithmetic_block",
            () -> BlockEntityType.Builder.of(
                    ArithmeticBlockEntity::new,
                    ModBlocks.ADD_BLOCK.get(),
                    ModBlocks.SUBTRACT_BLOCK.get(),
                    ModBlocks.MULTIPLY_BLOCK.get(),
                    ModBlocks.DIVIDE_BLOCK.get(),
                    ModBlocks.POWER_BLOCK.get()
            ).build(null)
    );

    private ModBlockEntities() {
    }
}
