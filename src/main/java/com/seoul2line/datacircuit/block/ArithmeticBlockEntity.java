package com.seoul2line.datacircuit.block;

import com.seoul2line.datacircuit.data.DataValue;
import com.seoul2line.datacircuit.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ArithmeticBlockEntity extends BlockEntity {
    private DataValue inputA = DataValue.number(0);
    private DataValue inputB = DataValue.number(0);

    public ArithmeticBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARITHMETIC_BLOCK.get(), pos, state);
    }

    public void setInputA(DataValue inputA) {
        this.inputA = inputA;
        setChanged();
    }

    public void setInputB(DataValue inputB) {
        this.inputB = inputB;
        setChanged();
    }

    public DataValue inputA() {
        return inputA;
    }

    public DataValue inputB() {
        return inputB;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("InputA", inputA.save());
        tag.put("InputB", inputB.save());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("InputA")) {
            inputA = DataValue.load(tag.getCompound("InputA"));
        }
        if (tag.contains("InputB")) {
            inputB = DataValue.load(tag.getCompound("InputB"));
        }
    }
}
