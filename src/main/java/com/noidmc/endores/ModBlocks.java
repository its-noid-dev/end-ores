package com.noidmc.endores;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MinecraftEndores.MODID);

    public static final DeferredBlock<Block> END_ORE = BLOCKS.registerSimpleBlock(
            "end_ore",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(65.0F, 1200.0F)
                    .requiresCorrectToolForDrops());

    private ModBlocks() {}
}
