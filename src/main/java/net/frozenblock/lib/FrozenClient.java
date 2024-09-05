/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.frozenblock.lib;

import net.frozenblock.lib.entrypoint.api.FrozenClientEntrypoint;
import net.frozenblock.lib.event.api.ClientEvent;
import net.frozenblock.lib.integration.api.ModIntegrations;
import net.frozenblock.lib.menu.api.Panoramas;
import net.frozenblock.lib.networking.FrozenClientNetworking;
import net.frozenblock.lib.screenshake.api.client.ScreenShaker;
import net.frozenblock.lib.sound.api.FlyBySoundHub;
import net.frozenblock.lib.sound.impl.block_sound_group.BlockSoundGroupManager;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.quiltmc.qsl.frozenblock.core.registry.impl.sync.client.ClientRegistrySync;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.impl.QuiltDataFixesInternals;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FrozenSharedConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrozenClient {

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        ModIntegrations.initializePreFreeze(); // Mod integrations must run after normal mod initialization

        // QUILT INIT
        ClientRegistrySync.registerHandlers();

        // CONTINUE FROZENLIB INIT

        // PARTICLES TODO: ADD PARTICLE FACTORY!
        //ParticleFactoryRegistry particleRegistry = ParticleFactoryRegistry.getInstance();

        //particleRegistry.register(FrozenParticleTypes.DEBUG_POS, DebugPosParticle.Provider::new);

        Panoramas.addPanorama(ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama"));

        NeoForge.EVENT_BUS.post(new FrozenClientEntrypoint());

    }

    @SubscribeEvent
    public static void registerClientResourceManager(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(BlockSoundGroupManager.INSTANCE);
    }

    @SubscribeEvent
    public static void clientFreezer(ClientEvent.Started event) {
        FrozenLogUtils.log("[Quilt DFU API] Clientside DataFixer Registry is about to freeze", true);
        QuiltDataFixesInternals.get().freeze();
        FrozenLogUtils.log("[Quilt DFU API] Clientside DataFixer Registry was frozen", true);
    }

    @SubscribeEvent
    public static void startWorldTick(ClientEvent.LevelStart event) {
        ClientWindManager.tick(event.source);
        ScreenShaker.tick(event.source);
        FlyBySoundHub.update(Minecraft.getInstance(), Minecraft.getInstance().cameraEntity, true);
    }

    @SubscribeEvent
    public static void clientTickStart(ClientEvent.Started event) {
        ClientWindManager.clearAndSwitchWindDisturbances();
    }

    public static void disconnect() {
        ScreenShaker.clear();
        ClientWindManager.clearAllWindDisturbances();
    }



}
