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

package net.frozenblock.lib.core.impl;

import net.frozenblock.lib.worldgen.surface.impl.OptimizedBiomeTagConditionSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class DataPackReloadMarker {

	private static boolean RELOADED = false;

	@SubscribeEvent
	public static void addResourceListeners(AddReloadListenerEvent event) {
		event.addListener((ResourceManagerReloadListener) resourceManager -> markReloaded());
	}

	@SubscribeEvent
	public static void startServerTick(ServerTickEvent.Pre event) {
		if(markedReloaded()) {
			OptimizedBiomeTagConditionSource.optimizeAll(event.getServer().registryAccess().registryOrThrow(Registries.BIOME));
		}
	}

	@SubscribeEvent
	public static void endServerTick(ServerTickEvent.Post event) {
		if (markedReloaded()) {
			unmarkReloaded();
		}
	}

	public static void markReloaded() {
		RELOADED = true;
	}

	public static void unmarkReloaded() {
		RELOADED = false;
	}

	public static boolean markedReloaded() {
		return RELOADED;
	}
}
