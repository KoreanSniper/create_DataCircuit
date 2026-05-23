package com.seoul2line.datacircuit.ponder;

import com.seoul2line.datacircuit.DataCircuitMod;
import com.seoul2line.datacircuit.registry.ModItems;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.api.registration.SharedTextRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class DataCircuitPonderPlugin implements PonderPlugin {
    public static final ResourceLocation DATA_CIRCUITS = new ResourceLocation(DataCircuitMod.MOD_ID, "data_circuits");

    @Override
    public String getModId() {
        return DataCircuitMod.MOD_ID;
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {
        helper.registerSharedText("datacircuit_link_rule", "DataCircuit wires are explicit links. Nearby blocks do not receive data unless a wire node is actually connected.");
        helper.registerSharedText("datacircuit_create_note", "Create: DataCircuit is ready for Create packs. Wire links are world-position based, so moving contraptions will need dedicated integration next.");
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(item -> ForgeRegistries.ITEMS.getKey(item.asItem()));
        helper.registerTag(DATA_CIRCUITS)
                .item(ModItems.DATA_WIRE_NODE.get())
                .title("Create: DataCircuit")
                .description("Explicit data wires, Boolean logic, and future typed computation for Create-style automation.")
                .addToIndex()
                .register();

        itemHelper.addToTag(DATA_CIRCUITS)
                .add(ModItems.DATA_WIRE_NODE.get())
                .add(ModItems.DATA_SIGNAL_SOURCE.get())
                .add(ModItems.DATA_LAMP.get())
                .add(ModItems.NUMBER_INPUT.get())
                .add(ModItems.TEXT_INPUT.get())
                .add(ModItems.RGB_SELECTOR.get())
                .add(ModItems.ADD_BLOCK.get())
                .add(ModItems.SUBTRACT_BLOCK.get())
                .add(ModItems.MULTIPLY_BLOCK.get())
                .add(ModItems.DIVIDE_BLOCK.get())
                .add(ModItems.POWER_BLOCK.get())
                .add(ModItems.AND_GATE.get())
                .add(ModItems.OR_GATE.get())
                .add(ModItems.XOR_GATE.get())
                .add(ModItems.NOT_GATE.get());
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(item -> ForgeRegistries.ITEMS.getKey(item.asItem()));

        itemHelper.forComponents(ModItems.DATA_WIRE_NODE.get())
                .addStoryBoard("data_wire_node", DataCircuitPonderScenes::wireNode, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.DATA_SIGNAL_SOURCE.get())
                .addStoryBoard("data_signal_source", DataCircuitPonderScenes::signalSource, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.DATA_LAMP.get())
                .addStoryBoard("data_lamp", DataCircuitPonderScenes::dataLamp, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.NUMBER_INPUT.get())
                .addStoryBoard("number_input", DataCircuitPonderScenes::numberInput, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.TEXT_INPUT.get())
                .addStoryBoard("text_input", DataCircuitPonderScenes::textInput, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.RGB_SELECTOR.get())
                .addStoryBoard("rgb_selector", DataCircuitPonderScenes::rgbSelector, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.ADD_BLOCK.get())
                .addStoryBoard("add_block", DataCircuitPonderScenes::addBlock, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.SUBTRACT_BLOCK.get())
                .addStoryBoard("subtract_block", DataCircuitPonderScenes::subtractBlock, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.MULTIPLY_BLOCK.get())
                .addStoryBoard("multiply_block", DataCircuitPonderScenes::multiplyBlock, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.DIVIDE_BLOCK.get())
                .addStoryBoard("divide_block", DataCircuitPonderScenes::divideBlock, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.POWER_BLOCK.get())
                .addStoryBoard("power_block", DataCircuitPonderScenes::powerBlock, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.AND_GATE.get())
                .addStoryBoard("and_gate", DataCircuitPonderScenes::andGate, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.OR_GATE.get())
                .addStoryBoard("or_gate", DataCircuitPonderScenes::orGate, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.XOR_GATE.get())
                .addStoryBoard("xor_gate", DataCircuitPonderScenes::xorGate, DATA_CIRCUITS);
        itemHelper.forComponents(ModItems.NOT_GATE.get())
                .addStoryBoard("not_gate", DataCircuitPonderScenes::notGate, DATA_CIRCUITS);
    }
}
