package com.seoul2line.datacircuit.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.seoul2line.datacircuit.DataCircuitMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DataCircuitMod.MOD_ID, value = Dist.CLIENT)
public class DataCircuitAnalysisTooltips {
    private DataCircuitAnalysisTooltips() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null || !DataCircuitMod.MOD_ID.equals(itemId.getNamespace())) {
            return;
        }

        if (isShiftDown()) {
            event.getToolTip().add(Component.translatable("tooltip.datacircuit.analysis.title").withStyle(ChatFormatting.AQUA));
            addAnalysis(event, itemId.getPath());
            event.getToolTip().add(Component.translatable("tooltip.datacircuit.analysis.create_ready").withStyle(ChatFormatting.DARK_AQUA));
        } else {
            event.getToolTip().add(Component.translatable("tooltip.datacircuit.analysis.hold_shift").withStyle(ChatFormatting.GRAY));
        }
    }

    private static boolean isShiftDown() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getWindow() == null) {
            return false;
        }
        long window = minecraft.getWindow().getWindow();
        return InputConstants.isKeyDown(window, InputConstants.KEY_LSHIFT)
                || InputConstants.isKeyDown(window, InputConstants.KEY_RSHIFT);
    }

    private static void addAnalysis(ItemTooltipEvent event, String path) {
        String key = switch (path) {
            case "data_wire_node" -> "tooltip.datacircuit.analysis.data_wire_node";
            case "data_signal_source" -> "tooltip.datacircuit.analysis.data_signal_source";
            case "data_lamp" -> "tooltip.datacircuit.analysis.data_lamp";
            case "number_input" -> "tooltip.datacircuit.analysis.number_input";
            case "text_input" -> "tooltip.datacircuit.analysis.text_input";
            case "rgb_selector" -> "tooltip.datacircuit.analysis.rgb_selector";
            case "add_block" -> "tooltip.datacircuit.analysis.add_block";
            case "subtract_block" -> "tooltip.datacircuit.analysis.subtract_block";
            case "multiply_block" -> "tooltip.datacircuit.analysis.multiply_block";
            case "divide_block" -> "tooltip.datacircuit.analysis.divide_block";
            case "power_block" -> "tooltip.datacircuit.analysis.power_block";
            case "and_gate" -> "tooltip.datacircuit.analysis.and_gate";
            case "or_gate" -> "tooltip.datacircuit.analysis.or_gate";
            case "xor_gate" -> "tooltip.datacircuit.analysis.xor_gate";
            case "not_gate" -> "tooltip.datacircuit.analysis.not_gate";
            default -> "tooltip.datacircuit.analysis.generic";
        };
        event.getToolTip().add(Component.translatable(key).withStyle(ChatFormatting.WHITE));
        addDetails(event, path);
        if (path.endsWith("_block")) {
            event.getToolTip().add(Component.translatable("tooltip.datacircuit.analysis.conversion_rules").withStyle(ChatFormatting.GRAY));
        }
    }

    private static void addDetails(ItemTooltipEvent event, String path) {
        switch (path) {
            case "data_wire_node" -> addDetail(event, "tooltip.datacircuit.analysis.data_wire_node.example");
            case "data_signal_source" -> addDetail(event, "tooltip.datacircuit.analysis.data_signal_source.example");
            case "data_lamp" -> addDetail(event, "tooltip.datacircuit.analysis.data_lamp.example");
            case "number_input" -> addDetail(event, "tooltip.datacircuit.analysis.number_input.example");
            case "text_input" -> addDetail(event, "tooltip.datacircuit.analysis.text_input.example");
            case "rgb_selector" -> addDetail(event, "tooltip.datacircuit.analysis.rgb_selector.example");
            case "add_block" -> {
                addDetail(event, "tooltip.datacircuit.analysis.add_block.example_1");
                addDetail(event, "tooltip.datacircuit.analysis.add_block.example_2");
            }
            case "subtract_block" -> {
                addDetail(event, "tooltip.datacircuit.analysis.subtract_block.example_1");
                addDetail(event, "tooltip.datacircuit.analysis.subtract_block.example_2");
            }
            case "multiply_block" -> {
                addDetail(event, "tooltip.datacircuit.analysis.multiply_block.example_1");
                addDetail(event, "tooltip.datacircuit.analysis.multiply_block.example_2");
            }
            case "divide_block" -> {
                addDetail(event, "tooltip.datacircuit.analysis.divide_block.example_1");
                addDetail(event, "tooltip.datacircuit.analysis.divide_block.example_2");
            }
            case "power_block" -> {
                addDetail(event, "tooltip.datacircuit.analysis.power_block.example_1");
                addDetail(event, "tooltip.datacircuit.analysis.power_block.example_2");
            }
            case "and_gate" -> addDetail(event, "tooltip.datacircuit.analysis.and_gate.example");
            case "or_gate" -> addDetail(event, "tooltip.datacircuit.analysis.or_gate.example");
            case "xor_gate" -> addDetail(event, "tooltip.datacircuit.analysis.xor_gate.example");
            case "not_gate" -> addDetail(event, "tooltip.datacircuit.analysis.not_gate.example");
            default -> {
            }
        }
    }

    private static void addDetail(ItemTooltipEvent event, String key) {
        event.getToolTip().add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
    }
}
