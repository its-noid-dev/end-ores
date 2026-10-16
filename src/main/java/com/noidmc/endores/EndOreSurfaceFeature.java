package com.noidmc.endores;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EndOreSurfaceFeature extends Feature<NoneFeatureConfiguration> {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, MinecraftEndores.MODID);

    public static final DeferredHolder<Feature<?>, EndOreSurfaceFeature> SURFACE_ORE =
            FEATURES.register("end_ore_surface", EndOreSurfaceFeature::new);

    public EndOreSurfaceFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos origin = context.origin();
        boolean placed = placeAtSurface(context.level(), origin.getX(), origin.getZ());

        for (int i = 0; i < 8; i++) {
            int x = origin.getX() + context.random().nextInt(5) - 2;
            int z = origin.getZ() + context.random().nextInt(5) - 2;
            placed |= placeAtSurface(context.level(), x, z);
        }

        return placed;
    }

    private static boolean placeAtSurface(WorldGenLevel level, int x, int z) {
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        BlockPos surface = new BlockPos(x, y, z);
        if (!level.getBlockState(surface).is(Blocks.END_STONE)
                || !level.getBlockState(surface.above()).isAir()) {
            return false;
        }

        return level.setBlock(surface, ModBlocks.END_ORE.get().defaultBlockState(), 2);
    }
}
