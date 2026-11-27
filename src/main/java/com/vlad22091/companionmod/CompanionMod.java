package com.vlad22091.companionmod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.vlad22091.companionmod.entity.ModEntities;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(CompanionMod.MODID)
public class CompanionMod {
    public static final String MODID = "companionmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<DeferredSpawnEggItem> COMPANION_SPAWN_EGG = ITEMS.register("companion_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.COMPANION, 0xE8A0BF, 0x7A3E5C, new Item.Properties()));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HEARTHMATES_TAB =
            CREATIVE_MODE_TABS.register("hearthmates_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.companionmod"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> COMPANION_SPAWN_EGG.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(COMPANION_SPAWN_EGG.get()))
                    .build());

    public CompanionMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Hearthmates: common setup");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(COMPANION_SPAWN_EGG);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Hearthmates: server starting");
    }
}