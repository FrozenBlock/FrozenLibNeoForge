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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class FrozenClient {

    public static void onInitializeClient(FMLClientSetupEvent event) {
        /*ModIntegrations.initializePreFreeze(); // Mod integrations must run after normal mod initialization

        // QUILT INIT
        ClientFreezer.onInitializeClient();
        ClientRegistrySync.registerHandlers();

        // CONTINUE FROZENLIB INIT
        registerClientEvents();
        FrozenClientNetworking.registerClientReceivers();

        // PARTICLES
        ParticleFactoryRegistry particleRegistry = ParticleFactoryRegistry.getInstance();

        particleRegistry.register(FrozenParticleTypes.DEBUG_POS, DebugPosParticle.Provider::new);

        Panoramas.addPanorama(ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama"));

        var resourceLoader = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        resourceLoader.registerReloadListener(BlockSoundGroupManager.INSTANCE);

        FrozenClientEntrypoint.EVENT.invoker().init(); // also includes dev init*/
    }

    private static void registerClientEvents() {

    }
}
