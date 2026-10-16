package com.noidmc.endores;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MinecraftEndores.MODID)
public class MinecraftEndores {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "minecraftendores";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "minecraftendores" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Create the End Ores tab with the mod's items.
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> END_ORES_TAB = CREATIVE_MODE_TABS.register("end_ores", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.minecraftendores.end_ores"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.END_PICKAXE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.END_ORE.get());
                output.accept(ModItems.END_INGOT.get());
                output.accept(ModItems.END_SWORD.get());
                output.accept(ModItems.END_PICKAXE.get());
                output.accept(ModItems.END_AXE.get());
                output.accept(ModItems.END_SHOVEL.get());
                output.accept(ModItems.END_HOE.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MinecraftEndores(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        EndOreSurfaceFeature.FEATURES.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (MinecraftEndores) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    @SubscribeEvent
    public void boostEndToolDamage(LivingDamageEvent.Pre event) {
        if (!event.getEntity().level().dimension().equals(Level.END) || !(event.getEntity() instanceof Mob)) {
            return;
        }

        var source = event.getSource();
        if (source.getDirectEntity() != source.getEntity() || !(source.getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        var heldItem = attacker.getMainHandItem();
        if (heldItem.is(ModItems.END_SWORD.get())
                || heldItem.is(ModItems.END_PICKAXE.get())
                || heldItem.is(ModItems.END_AXE.get())
                || heldItem.is(ModItems.END_SHOVEL.get())
                || heldItem.is(ModItems.END_HOE.get())) {
            event.setNewDamage(event.getNewDamage() * 1.5F);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
