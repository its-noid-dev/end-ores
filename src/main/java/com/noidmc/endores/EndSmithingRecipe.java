package com.noidmc.endores;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

public record EndSmithingRecipe(Ingredient base, Ingredient addition, ItemStack result) implements SmithingRecipe {
    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return input.template().isEmpty() && base.test(input.base()) && addition.test(input.addition());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack resultStack = input.base().transmuteCopy(result.getItem(), result.getCount());
        resultStack.applyComponents(result.getComponentsPatch());
        return resultStack;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, base, addition);
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return stack.isEmpty();
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return base.test(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return addition.test(stack);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.END_SMITHING_SERIALIZER.get();
    }

    @Override
    public boolean isIncomplete() {
        return base.hasNoItems() || addition.hasNoItems();
    }

    public static final class Serializer implements RecipeSerializer<EndSmithingRecipe> {
        private static final MapCodec<EndSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.CODEC.fieldOf("base").forGetter(EndSmithingRecipe::base),
                        Ingredient.CODEC.fieldOf("addition").forGetter(EndSmithingRecipe::addition),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(EndSmithingRecipe::result))
                        .apply(instance, EndSmithingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, EndSmithingRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<EndSmithingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EndSmithingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static EndSmithingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new EndSmithingRecipe(base, addition, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, EndSmithingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.addition());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result());
        }
    }
}
