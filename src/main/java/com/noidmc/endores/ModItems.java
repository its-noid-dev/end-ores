package com.noidmc.endores;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("minecraftendores");

    public static final DeferredItem<BlockItem> END_ORE =
            ITEMS.registerSimpleBlockItem("end_ore", ModBlocks.END_ORE);
    public static final DeferredItem<Item> END_INGOT =
            ITEMS.registerSimpleItem("end_ingot");

    public static final Tier END_TIER = new SimpleTier(
            Tiers.DIAMOND.getIncorrectBlocksForDrops(),
            1800,
            10.0F,
            3.5F,
            12,
            () -> Ingredient.of(END_INGOT.get()));

    public static final DeferredItem<SwordItem> END_SWORD = ITEMS.register(
            "end_sword",
            () -> new SwordItem(END_TIER, new Item.Properties()
                    .attributes(SwordItem.createAttributes(END_TIER, 3.5F, -2.0F))));
    public static final DeferredItem<PickaxeItem> END_PICKAXE = ITEMS.register(
            "end_pickaxe",
            () -> new PickaxeItem(END_TIER, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(END_TIER, 1.5F, -2.5F))));
    public static final DeferredItem<AxeItem> END_AXE = ITEMS.register(
            "end_axe",
            () -> new AxeItem(END_TIER, new Item.Properties()
                    .attributes(AxeItem.createAttributes(END_TIER, 5.5F, -2.7F))));
    public static final DeferredItem<ShovelItem> END_SHOVEL = ITEMS.register(
            "end_shovel",
            () -> new ShovelItem(END_TIER, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(END_TIER, 2.0F, -2.7F))));
    public static final DeferredItem<HoeItem> END_HOE = ITEMS.register(
            "end_hoe",
            () -> new HoeItem(END_TIER, new Item.Properties()
                    .attributes(HoeItem.createAttributes(END_TIER, -2.5F, 0.3F))));

    private ModItems() {}
}