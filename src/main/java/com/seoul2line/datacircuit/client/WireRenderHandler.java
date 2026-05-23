package com.seoul2line.datacircuit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.seoul2line.datacircuit.DataCircuitMod;
import com.seoul2line.datacircuit.data.DataPulseVisual;
import com.seoul2line.datacircuit.network.WireConnection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = DataCircuitMod.MOD_ID, value = Dist.CLIENT)
public class WireRenderHandler {
    private static final int SEGMENTS = 12;
    private static final float SAG = 0.22F;
    private static final float EDGE_DELAY = 0.18F;

    private WireRenderHandler() {
    }

    @SubscribeEvent
    public static void renderWires(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || ClientWireNetwork.connections().isEmpty()) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        VertexConsumer wireConsumer = bufferSource.getBuffer(RenderType.lines());
        for (WireConnection connection : ClientWireNetwork.connections()) {
            drawSaggingWire(poseStack, wireConsumer, cameraPos, connection);
        }

        renderPulses(minecraft, poseStack, bufferSource, camera, cameraPos, ClientWireNetwork.connections(), ClientWireNetwork.pulses());
        bufferSource.endBatch(RenderType.lines());
    }

    private static void drawSaggingWire(PoseStack poseStack, VertexConsumer consumer, Vec3 cameraPos, WireConnection connection) {
        Vec3 previous = curvePoint(connection.from(), connection.to(), 0.0F).subtract(cameraPos);
        for (int i = 1; i <= SEGMENTS; i++) {
            float t = i / (float) SEGMENTS;
            Vec3 current = curvePoint(connection.from(), connection.to(), t).subtract(cameraPos);
            drawLine(poseStack, consumer, previous, current, 0.15F, 0.95F, 1.0F, 0.92F);
            previous = current;
        }
    }

    private static void drawLine(PoseStack poseStack, VertexConsumer consumer, Vec3 from, Vec3 to, float red, float green, float blue, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();
        Vec3 direction = to.subtract(from).normalize();
        consumer.vertex(matrix, (float) from.x, (float) from.y, (float) from.z)
                .color(red, green, blue, alpha)
                .normal(normal, (float) direction.x, (float) direction.y, (float) direction.z)
                .endVertex();
        consumer.vertex(matrix, (float) to.x, (float) to.y, (float) to.z)
                .color(red, green, blue, alpha)
                .normal(normal, (float) direction.x, (float) direction.y, (float) direction.z)
                .endVertex();
    }

    private static void renderPulses(
            Minecraft minecraft,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            Camera camera,
            Vec3 cameraPos,
            List<WireConnection> connections,
            List<ClientWireNetwork.Pulse> pulses
    ) {
        Font font = minecraft.font;
        long now = System.currentTimeMillis();
        for (ClientWireNetwork.Pulse pulse : pulses) {
            float age = (now - pulse.startedAtMillis()) / 900.0F;
            if (age < 0.0F || age > 1.2F) {
                continue;
            }

            for (EdgePulse edgePulse : collectPulseEdges(connections, pulse.source())) {
                float progress = age - edgePulse.depth() * EDGE_DELAY;
                if (progress < 0.0F || progress > 1.0F) {
                    continue;
                }

                Vec3 worldPos = curvePoint(edgePulse.from(), edgePulse.to(), progress);
                renderPulseVisual(font, poseStack, bufferSource, camera, worldPos.subtract(cameraPos), pulse.visual());
            }
        }
    }

    private static List<EdgePulse> collectPulseEdges(List<WireConnection> connections, BlockPos source) {
        List<EdgePulse> edges = new ArrayList<>();
        Queue<NodeDepth> queue = new ArrayDeque<>();
        Set<BlockPos> visitedNodes = new HashSet<>();
        Set<WireConnection> visitedEdges = new HashSet<>();

        queue.add(new NodeDepth(source, 0));
        while (!queue.isEmpty()) {
            NodeDepth current = queue.remove();
            if (!visitedNodes.add(current.pos())) {
                continue;
            }

            for (WireConnection connection : connections) {
                BlockPos next = null;
                if (connection.from().equals(current.pos())) {
                    next = connection.to();
                } else if (connection.to().equals(current.pos())) {
                    next = connection.from();
                }

                if (next == null || visitedNodes.contains(next)) {
                    continue;
                }

                WireConnection normalized = connection.normalized();
                if (visitedEdges.add(normalized)) {
                    edges.add(new EdgePulse(current.pos(), next, current.depth()));
                }
                queue.add(new NodeDepth(next, current.depth() + 1));
            }
        }

        return edges;
    }

    private static void renderPulseVisual(Font font, PoseStack poseStack, MultiBufferSource bufferSource, Camera camera, Vec3 localPos, DataPulseVisual visual) {
        poseStack.pushPose();
        poseStack.translate(localPos.x, localPos.y + 0.08D, localPos.z);
        poseStack.mulPose(camera.rotation());
        poseStack.scale(-0.028F, -0.028F, 0.028F);

        String text = visual.label();
        float width = font.width(text) / 2.0F;
        if (visual.rgbPreview()) {
            font.drawInBatch(
                    "RGB",
                    -width - 20.0F,
                    0.0F,
                    visual.color(),
                    false,
                    poseStack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.SEE_THROUGH,
                    0,
                    LightTexture.FULL_BRIGHT
            );
        }
        font.drawInBatch(
                text,
                -width,
                0.0F,
                visual.color(),
                false,
                poseStack.last().pose(),
                bufferSource,
                Font.DisplayMode.SEE_THROUGH,
                0,
                LightTexture.FULL_BRIGHT
        );
        poseStack.popPose();
    }

    private static Vec3 curvePoint(BlockPos from, BlockPos to, float t) {
        Vec3 a = Vec3.atCenterOf(from);
        Vec3 b = Vec3.atCenterOf(to);
        double x = lerp(a.x, b.x, t);
        double y = lerp(a.y, b.y, t) - Math.sin(Math.PI * t) * SAG;
        double z = lerp(a.z, b.z, t);
        return new Vec3(x, y, z);
    }

    private static double lerp(double from, double to, float t) {
        return from + (to - from) * t;
    }

    private record NodeDepth(BlockPos pos, int depth) {
    }

    private record EdgePulse(BlockPos from, BlockPos to, int depth) {
    }
}
