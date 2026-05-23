package com.seoul2line.datacircuit.client;

import com.seoul2line.datacircuit.block.InputKind;
import com.seoul2line.datacircuit.network.ModNetworking;
import com.seoul2line.datacircuit.network.SubmitTypedInputPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DataTypedInputScreen extends Screen {
    private final BlockPos pos;
    private final InputKind kind;
    private EditBox input;

    public DataTypedInputScreen(BlockPos pos, InputKind kind) {
        super(Component.translatable(kind.titleKey()));
        this.pos = pos;
        this.kind = kind;
    }

    @Override
    protected void init() {
        int panelX = width / 2 - 110;
        int panelY = height / 2 - 46;
        input = new EditBox(font, panelX, panelY + 28, 220, 20, Component.translatable("screen.datacircuit.input"));
        input.setMaxLength(64);
        input.setValue(kind.defaultInput());
        addRenderableWidget(input);
        setInitialFocus(input);

        if (kind == InputKind.RGB) {
            addPresetButton(panelX, panelY + 56, "#FF0000");
            addPresetButton(panelX + 56, panelY + 56, "#33AAFF");
            addPresetButton(panelX + 112, panelY + 56, "#55FF55");
            addPresetButton(panelX + 168, panelY + 56, "#FFFFFF");
        }

        addRenderableWidget(Button.builder(Component.translatable("screen.datacircuit.send"), button -> submit())
                .bounds(panelX, panelY + 86, 104, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(panelX + 116, panelY + 86, 104, 20)
                .build());
    }

    private void addPresetButton(int x, int y, String value) {
        addRenderableWidget(Button.builder(Component.literal(value), button -> input.setValue(value))
                .bounds(x, y, 52, 20)
                .build());
    }

    private void submit() {
        ModNetworking.CHANNEL.sendToServer(new SubmitTypedInputPacket(pos, kind, input.getValue()));
        onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) {
            submit();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 64, 0x55FFFF);
        graphics.drawString(font, Component.translatable("screen.datacircuit.input"), width / 2 - 110, height / 2 - 30, 0xEAEAEA);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
