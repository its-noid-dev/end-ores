package com.noidmc.endores;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MinecraftEndores.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EndSmithingRecipe>> END_SMITHING_SERIALIZER =
            SERIALIZERS.register("end_smithing", EndSmithingRecipe.Serializer::new);

    private ModRecipes() {}
}
