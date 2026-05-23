# Create: DataCircuit

Forge 1.20.1 data circuit mod prototype.

The internal mod id stays `datacircuit` because Minecraft mod ids cannot safely use a colon or uppercase letters. The display name is `Create: DataCircuit`.

## Current Blocks

- Data Wire Node: wall, floor, or ceiling mounted data port.
- Data Signal Source: toggled Boolean sender.
- Data Lamp: Boolean receiver that lights when the connected data network is true.
- AND Gate, OR Gate, XOR Gate, NOT Gate: first Boolean logic modules.

## Create Compatibility

`Create` is declared as an optional dependency. This means the mod can run by itself while staying ready to load after Create when Create is present.

Current compatibility level:

- DataCircuit blocks use normal block/item registration and can exist in Create-focused packs.
- Tooltips use a Create/Ponder-style `W` analysis prompt for every DataCircuit item.
- Circuit logic remains position-based, so explicit wire links should be rebuilt after moving blocks with any contraption-like system.

Planned next step:

- Add real Create Ponder scenes through Create/Ponder API once Create is added to the development and test instance.
- Add true contraption movement handling so saved wire links can follow moved assemblies instead of staying at old world positions.

The Create developer wiki lists Create 6.0.8 and Ponder 1.0.91 for Forge 1.20.1 development dependencies. This project is not compiling against those APIs yet, so the current build remains lightweight and can still run without Create installed.

## Data Wire Behavior

DataCircuit wires are explicit links, not redstone-style neighbor power.

1. Right-click a data port to select the first endpoint.
2. Right-click another data port to create a link.
3. Shift-right-click a data port to remove every link touching that port.
4. If a data port is selected and you break any block, the pending selection is cancelled.
5. If a data port is selected and you shift-right-click a block that is not a data connector, the pending selection is cancelled.

When a wire is added or removed, the mod wakes up connected ports and their attached blocks up to 10 wire hops away. This keeps nearby gates and receivers from staying stale after the wire graph changes, without updating the whole world.

## Logic Gate Ports

Place Data Wire Nodes on gate faces to use them as ports.

- AND / OR / XOR:
  - Left side of the gate: input A.
  - Right side of the gate: input B.
  - Front side of the gate: output.
- NOT:
  - Back side of the gate: input.
  - Front side of the gate: output.

The front side is the direction the gate block faces. Input and output networks are intentionally separated so signals do not leak through the gate body.

## Visuals

Connected wires are rendered client-side as slightly sagging lines.
When a Boolean signal changes, `0` or `1` moves along the affected wire path as a visual effect. The visual does not add signal delay.

## Item Analysis

Hover any DataCircuit item and hold `W` to show an in-game analysis tooltip.
This is intentionally shaped like Create's Ponder interaction, but it is currently an internal tooltip system rather than a full animated Ponder scene.

## Textures

Block textures should use custom DataCircuit paths under:

```text
src/main/resources/assets/datacircuit/textures/block
```

## Build

Use Java 17 and ForgeGradle from the project root:

```powershell
$env:GRADLE_USER_HOME=(Join-Path (Get-Location) '.gradle-home'); & "$env:USERPROFILE\.gradle\wrapper\dists\gradle-8.8-bin\dl7vupf4psengwqhwktix4v1\gradle-8.8\bin\gradle.bat" build --no-daemon --console=plain
```

Successful builds automatically deploy the jar to the CurseForge `DataCircuit` instance.
