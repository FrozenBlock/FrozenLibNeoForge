package net.frozenblock.lib;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.config.impl.ConfigCommand;
import net.frozenblock.lib.entity.api.EntityUtils;
import net.frozenblock.lib.entity.api.command.ScaleEntityCommand;
import net.frozenblock.lib.event.api.PlayerJoinEvent;
import net.frozenblock.lib.event.api.RegistryFreezeEvent;
import net.frozenblock.lib.integration.api.ModIntegrations;
import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.frozenblock.lib.screenshake.api.command.ScreenShakeCommand;
import net.frozenblock.lib.screenshake.impl.ScreenShakeStorage;
import net.frozenblock.lib.tag.api.TagListCommand;
import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.lib.wind.api.command.WindOverrideCommand;
import net.frozenblock.lib.wind.impl.WindStorage;
import net.minecraft.server.commands.WardenSpawnTrackerCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * A basic eventListener class
 * This class listens to NEO FORGE EVENT BUS only
 * */
public class FrozenEventListener {
    @SubscribeEvent
    public static void levelLoad(LevelEvent.Load loadEvent) {
        if(!loadEvent.getLevel().isClientSide()) {
            final ServerLevel level = (ServerLevel) loadEvent.getLevel();
            DimensionDataStorage dimensionDataStorage = level.getDataStorage();
            WindManager windManager = WindManager.getWindManager(level);
            dimensionDataStorage.computeIfAbsent(windManager.createData(), WindStorage.WIND_FILE_ID);
            ScreenShakeManager screenShakeManager = ScreenShakeManager.getScreenShakeManager(level);
            dimensionDataStorage.computeIfAbsent(screenShakeManager.createData(), ScreenShakeStorage.SCREEN_SHAKE_FILE_ID);
        }
    }

    @SubscribeEvent
    public static void levelUnload(LevelEvent.Unload unloadEvent) {
        if(!unloadEvent.getLevel().isClientSide()) {
            final ServerLevel serverLevel = (ServerLevel) unloadEvent.getLevel();
            EntityUtils.clearEntitiesPerLevel(serverLevel);
            WindManager.getWindManager(serverLevel).clearAllWindDisturbances();
        }
    }

    @SubscribeEvent
    public static void serverStartTick(LevelTickEvent.Pre pre) {
        if(!pre.getLevel().isClientSide) {
            final ServerLevel serverLevel = (ServerLevel) pre.getLevel();
            WindManager.getWindManager(serverLevel).clearAndSwitchWindDisturbances();
            WindManager.getWindManager(serverLevel).tick(serverLevel);
            ScreenShakeManager.getScreenShakeManager(serverLevel).tick(serverLevel);
            EntityUtils.populateEntitiesPerLevel(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onPlayerAddedTopLevel(PlayerJoinEvent.Level event) {
        WindManager windManager = WindManager.getWindManager(event.level);
        windManager.sendSyncToPlayer(windManager.createSyncPacket(), event.player);
    }

    @SubscribeEvent
    public static void startRegistryFreeze(RegistryFreezeEvent.Start event) {
        if (!event.allRegistries) return;

        ModIntegrations.initialize();
    }

    @SubscribeEvent
    public static void endRegistryFreeze(RegistryFreezeEvent.End event) {
        if (!event.allRegistries) return;

        for (Config<?> config : ConfigRegistry.getAllConfigs()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        final var dispatcher = event.getDispatcher();
        WindOverrideCommand.register(dispatcher);
        ScreenShakeCommand.register(dispatcher);
        ConfigCommand.register(dispatcher);
        TagListCommand.register(dispatcher);
        ScaleEntityCommand.register(dispatcher);

        if(FrozenLibConfig.get().wardenSpawnTrackerCommand)
            WardenSpawnTrackerCommand.register(dispatcher);
    }
}
