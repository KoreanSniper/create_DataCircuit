# DataCircuit Texture Rules

All DataCircuit block and item models should use textures from the `datacircuit` namespace.

Block textures go here:

```text
assets/datacircuit/textures/block/
```

Current required block textures:

```text
data_wire_node.png
data_signal_source.png
data_signal_source_on.png
data_lamp.png
data_lamp_on.png
and_gate.png
or_gate.png
xor_gate.png
not_gate.png
```

When adding a new block model, use this pattern:

```json
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "datacircuit:block/example_block"
  }
}
```

Using Minecraft parent models is fine. The texture paths should stay custom unless a vanilla texture is intentionally needed for debugging.
