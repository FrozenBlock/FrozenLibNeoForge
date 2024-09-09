/*
 * Copyright 2024 The Quilt Project
 * Copyright 2024 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.frozenblock.resource.loader.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.WorldDataConfiguration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import org.quiltmc.qsl.frozenblock.resource.loader.api.ResourceLoaderEvent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

/**
 * Modified to work on Fabric
 */
@OnlyIn(Dist.CLIENT)
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {

	@Dynamic
	@Inject(
			method = {"m_qcsfhvrb", "method_41851", "lambda$openFresh$2"},
			at = @At("HEAD"),
			require = 1
	)
	private static void onEndDataPackLoadOnOpen(CloseableResourceManager resourceManager, ReloadableServerResources resources,
			LayeredRegistryAccess<?> layeredRegistryAccess, @Coerce Object object, CallbackInfoReturnable<WorldCreationContext> cir) {
		NeoForge.EVENT_BUS.post(new ResourceLoaderEvent.EndDataPackReload(null, resourceManager, null));
	}

    @Inject(
            method = "applyNewPackConfig",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/WorldLoader;load(Lnet/minecraft/server/WorldLoader$InitConfig;Lnet/minecraft/server/WorldLoader$WorldDataSupplier;Lnet/minecraft/server/WorldLoader$ResultFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private void onDataPackLoadStart(PackRepository packRepository, WorldDataConfiguration worldDataConfiguration, Consumer<WorldDataConfiguration> consumer, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new ResourceLoaderEvent.StartDataPackReload(null, null));
    }

    @Inject(
            method = "openFresh",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/WorldLoader;load(Lnet/minecraft/server/WorldLoader$InitConfig;Lnet/minecraft/server/WorldLoader$WorldDataSupplier;Lnet/minecraft/server/WorldLoader$ResultFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private static void onDataPackLoadStart(Minecraft client, Screen parent, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new ResourceLoaderEvent.StartDataPackReload(null, null));
    }

	@Dynamic
	@Inject(
			method = { "lambda$applyNewPackConfig$14", "lambda$applyNewPackConfig$13"},
			at = @At("HEAD"),
			require = 1
	)
	private static void onCreateDataPackLoadEnd(CloseableResourceManager resourceManager, ReloadableServerResources resources,
			LayeredRegistryAccess<?> layeredRegistryAccess, @Coerce Object object, CallbackInfoReturnable<WorldCreationContext> cir) {
		NeoForge.EVENT_BUS.post(new ResourceLoaderEvent.EndDataPackReload(null, resourceManager, null));
	}

	@Inject(
			method = "lambda$applyNewPackConfig$17(Ljava/util/function/Consumer;Ljava/lang/Void;Ljava/lang/Throwable;)Ljava/lang/Object;",
			at = @At(
					value = "INVOKE",
					target = 	"Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V",
					shift = At.Shift.AFTER,
					remap = false
			)
	)
	private static void onFailDataPackLoading(Consumer<WorldDataConfiguration> consumer, Void unused, Throwable throwable, CallbackInfoReturnable<Object> cir) {
		NeoForge.EVENT_BUS.post(new ResourceLoaderEvent.EndDataPackReload(null, null, throwable));
	}
}
