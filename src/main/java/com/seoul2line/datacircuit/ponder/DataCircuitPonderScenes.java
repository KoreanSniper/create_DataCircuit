package com.seoul2line.datacircuit.ponder;

import com.seoul2line.datacircuit.block.DataLampBlock;
import com.seoul2line.datacircuit.block.DataSignalSourceBlock;
import com.seoul2line.datacircuit.block.LogicGateBlock;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class DataCircuitPonderScenes {
    private DataCircuitPonderScenes() {
    }

    public static void wireNode(SceneBuilder scene, SceneBuildingUtil util) {
        base(scene);
        scene.title("data_wire_node", "Explicit Data Wire Nodes");
        showSmallRig(scene, util);
        scene.overlay().showText(70)
                .text("Data Wire Nodes are mounted ports, not redstone dust.")
                .pointAt(util.vector().centerOf(1, 1, 2))
                .placeNearTarget();
        scene.idle(80);
        scene.overlay().showControls(util.vector().centerOf(1, 1, 2), Pointing.DOWN, 45).rightClick();
        scene.overlay().showControls(util.vector().centerOf(3, 1, 2), Pointing.DOWN, 45).rightClick();
        scene.overlay().showText(80)
                .sharedText("datacircuit_link_rule")
                .pointAt(util.vector().centerOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(90);
    }

    public static void signalSource(SceneBuilder scene, SceneBuildingUtil util) {
        base(scene);
        scene.title("data_signal_source", "Sending Boolean Data");
        showSmallRig(scene, util);
        scene.overlay().showControls(util.vector().topOf(0, 1, 2), Pointing.DOWN, 50).rightClick();
        setSource(scene, new BlockPos(0, 1, 2), true);
        setLamp(scene, new BlockPos(4, 1, 2), true);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(1, 1, 2)), Vec3.atCenterOf(new BlockPos(3, 1, 2)), 70);
        scene.overlay().showText(80)
                .text("Right-click a Data Signal Source to toggle a 0/1 signal.")
                .pointAt(util.vector().topOf(0, 1, 2))
                .placeNearTarget();
        scene.idle(90);
    }

    public static void dataLamp(SceneBuilder scene, SceneBuildingUtil util) {
        base(scene);
        scene.title("data_lamp", "Reading Data Outputs");
        showSmallRig(scene, util);
        scene.overlay().showText(70)
                .text("Data Lamps react only to their connected data network.")
                .pointAt(util.vector().topOf(4, 1, 2))
                .placeNearTarget();
        scene.idle(80);
        setSource(scene, new BlockPos(0, 1, 2), true);
        setLamp(scene, new BlockPos(4, 1, 2), true);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(1, 1, 2)), Vec3.atCenterOf(new BlockPos(3, 1, 2)), 80);
        scene.overlay().showText(80)
                .text("If multiple inputs feed one output, the lamp stays on while any input remains on.")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(4, 1, 2))
                .placeNearTarget();
        scene.idle(90);
    }

    public static void numberInput(SceneBuilder scene, SceneBuildingUtil util) {
        typedInput(scene, util, "number_input", "Number Input", "Right-click to enter a number, then send it as NUMBER data.", "Example: 42 travels through the connected wire.");
    }

    public static void textInput(SceneBuilder scene, SceneBuildingUtil util) {
        typedInput(scene, util, "text_input", "Text Input", "Right-click to enter text, then send it as TEXT data.", "Example: HELLO can be shown on Create displays or used by text arithmetic.");
    }

    public static void rgbSelector(SceneBuilder scene, SceneBuildingUtil util) {
        typedInput(scene, util, "rgb_selector", "RGB Selector", "Right-click to choose or type a HEX color.", "Example: #33AAFF travels as RGB data and is drawn in that color.");
    }

    public static void addBlock(SceneBuilder scene, SceneBuildingUtil util) {
        arithmetic(scene, util, "add_block", "Add Block", "Adds numbers, joins text with a space, and mixes RGB colors.", "2 + 3 = 5", "aaa + bbb = aaa bbb");
    }

    public static void subtractBlock(SceneBuilder scene, SceneBuildingUtil util) {
        arithmetic(scene, util, "subtract_block", "Subtract Block", "Subtracts numbers and removes the first matching text segment.", "10 - 4 = 6", "abcd - bc = ad");
    }

    public static void multiplyBlock(SceneBuilder scene, SceneBuildingUtil util) {
        arithmetic(scene, util, "multiply_block", "Multiply Block", "Multiplies numbers. Text times a number repeats the text.", "6 * 7 = 42", "aaa * 5 = aaaaaaaaaaaaaaa, but text * text = !");
    }

    public static void divideBlock(SceneBuilder scene, SceneBuildingUtil util) {
        arithmetic(scene, util, "divide_block", "Divide Block", "Divides numbers and removes every matching text segment.", "8 / 2 = 4", "aaabbbaaa / b = aaaaaa");
    }

    public static void powerBlock(SceneBuilder scene, SceneBuildingUtil util) {
        arithmetic(scene, util, "power_block", "Power Block", "Raises numbers to a power. Text and RGB output an error.", "2 ** 8 = 256", "TEXT ** 2 = ! and RGB ** 2 = !");
    }

    public static void andGate(SceneBuilder scene, SceneBuildingUtil util) {
        gate(scene, util, "and_gate", "AND Gate", "Both side inputs must be on before the front output turns on.");
        demonstrateAnd(scene, util);
    }

    public static void orGate(SceneBuilder scene, SceneBuildingUtil util) {
        gate(scene, util, "or_gate", "OR Gate", "Either side input can turn the front output on.");
        demonstrateOr(scene, util);
    }

    public static void xorGate(SceneBuilder scene, SceneBuildingUtil util) {
        gate(scene, util, "xor_gate", "XOR Gate", "The front output turns on only when the two side inputs differ.");
        demonstrateXor(scene, util);
    }

    public static void notGate(SceneBuilder scene, SceneBuildingUtil util) {
        base(scene);
        scene.title("not_gate", "NOT Gate");
        showNotRig(scene, util);
        scene.overlay().showText(70)
                .text("NOT uses the back as input and the front as inverted output.")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(80);
        scene.overlay().showText(80)
                .text("With no input, NOT starts on. A true input turns its output off.")
                .colored(PonderPalette.OUTPUT)
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        setGate(scene, new BlockPos(2, 1, 2), true, false, true);
        setLamp(scene, new BlockPos(2, 1, 0), true);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(2, 1, 2)), Vec3.atCenterOf(new BlockPos(2, 1, 0)), 65);
        scene.idle(75);

        scene.overlay().showControls(util.vector().topOf(2, 1, 4), Pointing.DOWN, 45).rightClick();
        setSource(scene, new BlockPos(2, 1, 4), true);
        setGate(scene, new BlockPos(2, 1, 2), true, false, false);
        setLamp(scene, new BlockPos(2, 1, 0), false);
        scene.overlay().showText(70)
                .text("When the back input becomes 1, the inverted front output becomes 0.")
                .colored(PonderPalette.RED)
                .pointAt(util.vector().topOf(2, 1, 0))
                .placeNearTarget();
        scene.idle(85);
    }

    private static void gate(SceneBuilder scene, SceneBuildingUtil util, String key, String title, String rule) {
        base(scene);
        scene.title(key, title);
        showGateRig(scene, util);
        scene.overlay().showText(70)
                .text("Attach wire nodes to the left and right sides for inputs.")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(80);
        scene.overlay().showText(80)
                .text(rule)
                .colored(PonderPalette.OUTPUT)
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(55);
    }

    private static void demonstrateAnd(SceneBuilder scene, SceneBuildingUtil util) {
        BlockPos leftSource = new BlockPos(0, 1, 2);
        BlockPos rightSource = new BlockPos(4, 1, 2);
        BlockPos gate = new BlockPos(2, 1, 2);
        BlockPos lamp = new BlockPos(2, 1, 0);

        scene.overlay().showControls(util.vector().topOf(leftSource), Pointing.DOWN, 45).rightClick();
        setSource(scene, leftSource, true);
        setGate(scene, gate, true, false, false);
        scene.overlay().showText(65)
                .text("One input is on, but AND still keeps the output off.")
                .colored(PonderPalette.RED)
                .pointAt(util.vector().topOf(lamp))
                .placeNearTarget();
        scene.idle(75);

        scene.overlay().showControls(util.vector().topOf(rightSource), Pointing.DOWN, 45).rightClick();
        setSource(scene, rightSource, true);
        setGate(scene, gate, true, true, true);
        setLamp(scene, lamp, true);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(gate), Vec3.atCenterOf(lamp), 70);
        scene.overlay().showText(65)
                .text("With both inputs on, the output finally turns on.")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(lamp))
                .placeNearTarget();
        scene.idle(80);
    }

    private static void demonstrateOr(SceneBuilder scene, SceneBuildingUtil util) {
        BlockPos leftSource = new BlockPos(0, 1, 2);
        BlockPos rightSource = new BlockPos(4, 1, 2);
        BlockPos gate = new BlockPos(2, 1, 2);
        BlockPos lamp = new BlockPos(2, 1, 0);

        scene.overlay().showControls(util.vector().topOf(leftSource), Pointing.DOWN, 45).rightClick();
        setSource(scene, leftSource, true);
        setGate(scene, gate, true, false, true);
        setLamp(scene, lamp, true);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(gate), Vec3.atCenterOf(lamp), 70);
        scene.overlay().showText(65)
                .text("OR turns on as soon as either input becomes 1.")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(lamp))
                .placeNearTarget();
        scene.idle(75);

        scene.overlay().showControls(util.vector().topOf(rightSource), Pointing.DOWN, 45).rightClick();
        setSource(scene, rightSource, true);
        setGate(scene, gate, true, true, true);
        scene.overlay().showText(60)
                .text("The second input can also be on; the output stays on.")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(gate))
                .placeNearTarget();
        scene.idle(75);
    }

    private static void demonstrateXor(SceneBuilder scene, SceneBuildingUtil util) {
        BlockPos leftSource = new BlockPos(0, 1, 2);
        BlockPos rightSource = new BlockPos(4, 1, 2);
        BlockPos gate = new BlockPos(2, 1, 2);
        BlockPos lamp = new BlockPos(2, 1, 0);

        scene.overlay().showControls(util.vector().topOf(leftSource), Pointing.DOWN, 45).rightClick();
        setSource(scene, leftSource, true);
        setGate(scene, gate, true, false, true);
        setLamp(scene, lamp, true);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(gate), Vec3.atCenterOf(lamp), 70);
        scene.overlay().showText(65)
                .text("XOR turns on when exactly one input is 1.")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(lamp))
                .placeNearTarget();
        scene.idle(75);

        scene.overlay().showControls(util.vector().topOf(rightSource), Pointing.DOWN, 45).rightClick();
        setSource(scene, rightSource, true);
        setGate(scene, gate, true, true, false);
        setLamp(scene, lamp, false);
        scene.overlay().showText(70)
                .text("When both inputs match, XOR turns the output back off.")
                .colored(PonderPalette.RED)
                .pointAt(util.vector().topOf(lamp))
                .placeNearTarget();
        scene.idle(80);
    }

    private static void typedInput(SceneBuilder scene, SceneBuildingUtil util, String key, String title, String description, String example) {
        base(scene);
        scene.title(key, title);
        showSmallRig(scene, util);
        scene.overlay().showText(75)
                .text(description)
                .pointAt(util.vector().topOf(0, 1, 2))
                .placeNearTarget();
        scene.idle(85);
        scene.overlay().showControls(util.vector().topOf(0, 1, 2), Pointing.DOWN, 45).rightClick();
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(1, 1, 2)), Vec3.atCenterOf(new BlockPos(3, 1, 2)), 75);
        scene.overlay().showText(80)
                .text(example)
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(90);
    }

    private static void arithmetic(SceneBuilder scene, SceneBuildingUtil util, String key, String title, String rule, String numberExample, String typedExample) {
        base(scene);
        scene.title(key, title);
        showGateRig(scene, util);
        scene.overlay().showText(70)
                .text("Left and right wire nodes are inputs; the front node is the result output.")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(80);
        scene.overlay().showLine(PonderPalette.INPUT, Vec3.atCenterOf(new BlockPos(1, 1, 2)), Vec3.atCenterOf(new BlockPos(2, 1, 2)), 70);
        scene.overlay().showLine(PonderPalette.INPUT, Vec3.atCenterOf(new BlockPos(3, 1, 2)), Vec3.atCenterOf(new BlockPos(2, 1, 2)), 70);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(2, 1, 2)), Vec3.atCenterOf(new BlockPos(2, 1, 0)), 70);
        scene.overlay().showText(75)
                .text(rule)
                .colored(PonderPalette.OUTPUT)
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(85);
        scene.overlay().showText(65)
                .text(numberExample)
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(2, 1, 0))
                .placeNearTarget();
        scene.idle(75);
        scene.overlay().showText(75)
                .text(typedExample)
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector().topOf(2, 1, 0))
                .placeNearTarget();
        scene.idle(85);
    }

    private static void showSmallRig(SceneBuilder scene, SceneBuildingUtil util) {
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(0, 1, 2, 4, 1, 2), Direction.DOWN);
        scene.overlay().showLine(PonderPalette.BLUE, Vec3.atCenterOf(new BlockPos(1, 1, 2)), Vec3.atCenterOf(new BlockPos(3, 1, 2)), 90);
        scene.idle(30);
    }

    private static void showGateRig(SceneBuilder scene, SceneBuildingUtil util) {
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(0, 1, 0, 4, 1, 2), Direction.DOWN);
        scene.overlay().showLine(PonderPalette.INPUT, Vec3.atCenterOf(new BlockPos(1, 1, 2)), Vec3.atCenterOf(new BlockPos(2, 1, 2)), 80);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(2, 1, 2)), Vec3.atCenterOf(new BlockPos(3, 1, 2)), 80);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(2, 1, 2)), Vec3.atCenterOf(new BlockPos(2, 1, 0)), 80);
        scene.idle(30);
    }

    private static void showNotRig(SceneBuilder scene, SceneBuildingUtil util) {
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(2, 1, 0, 2, 1, 4), Direction.DOWN);
        scene.overlay().showLine(PonderPalette.INPUT, Vec3.atCenterOf(new BlockPos(2, 1, 4)), Vec3.atCenterOf(new BlockPos(2, 1, 2)), 80);
        scene.overlay().showLine(PonderPalette.OUTPUT, Vec3.atCenterOf(new BlockPos(2, 1, 2)), Vec3.atCenterOf(new BlockPos(2, 1, 0)), 80);
        scene.idle(30);
    }

    private static void setSource(SceneBuilder scene, BlockPos pos, boolean powered) {
        scene.world().modifyBlock(pos, state -> state.setValue(DataSignalSourceBlock.POWERED, powered), false);
    }

    private static void setLamp(SceneBuilder scene, BlockPos pos, boolean lit) {
        scene.world().modifyBlock(pos, state -> state.setValue(DataLampBlock.LIT, lit), false);
    }

    private static void setGate(SceneBuilder scene, BlockPos pos, boolean inputA, boolean inputB, boolean powered) {
        scene.world().modifyBlock(pos, state -> state
                .setValue(LogicGateBlock.INPUT_A, inputA)
                .setValue(LogicGateBlock.INPUT_B, inputB)
                .setValue(LogicGateBlock.POWERED, powered), false);
    }

    private static void base(SceneBuilder scene) {
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(0.85F);
        scene.showBasePlate();
        scene.idle(10);
    }
}
