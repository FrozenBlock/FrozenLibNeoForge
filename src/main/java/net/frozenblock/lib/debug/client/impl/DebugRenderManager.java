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

package net.frozenblock.lib.debug.client.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig;
import net.frozenblock.lib.debug.client.api.DebugRendererEvent;
import net.frozenblock.lib.debug.client.renderer.ImprovedGameEventListenerRenderer;
import net.frozenblock.lib.debug.client.renderer.ImprovedGoalSelectorDebugRenderer;
import net.frozenblock.lib.debug.client.renderer.WindDebugRenderer;
import net.frozenblock.lib.debug.client.renderer.WindDisturbanceDebugRenderer;
import net.frozenblock.lib.event.api.ClientTickEvent;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class DebugRenderManager {
	public static Map<DebugRendererHolder, ResourceLocation> DEBUG_RENDERER_HOLDERS = new Object2ObjectLinkedOpenHashMap<>();
	private static final List<Runnable> ON_CLEAR_RUNNABLES = new ArrayList<>();

	public static void registerRenderer(ResourceLocation location, DebugRendererHolder.RenderInterface renderInterface) {
		if (location == null) throw new IllegalArgumentException("ResourceLocation cannot be null!");
		DEBUG_RENDERER_HOLDERS.put(new DebugRendererHolder(renderInterface), location);
		Stream<Map.Entry<DebugRendererHolder, ResourceLocation>> entries = DEBUG_RENDERER_HOLDERS.entrySet().stream()
			.sorted(Comparator.comparing(value ->  value.getValue().getPath()));

		Map<DebugRendererHolder, ResourceLocation> newRenderHolders = new Object2ObjectLinkedOpenHashMap<>();
		entries.forEach(entry -> newRenderHolders.put(entry.getKey(), entry.getValue()));

		DEBUG_RENDERER_HOLDERS = newRenderHolders;
	}

	public static final ImprovedGoalSelectorDebugRenderer improvedGoalSelectorRenderer = new ImprovedGoalSelectorDebugRenderer(Minecraft.getInstance());
	public static final ImprovedGameEventListenerRenderer improvedGameEventRenderer = new ImprovedGameEventListenerRenderer(Minecraft.getInstance());
	public static final WindDisturbanceDebugRenderer windDisturbanceDebugRenderer = new WindDisturbanceDebugRenderer(Minecraft.getInstance());
	public static final WindDebugRenderer windDebugRenderer = new WindDebugRenderer(Minecraft.getInstance());

	@SubscribeEvent
	public static void debugRenderer(final DebugRendererEvent event) {
		addClearRunnable(improvedGoalSelectorRenderer::clear);

		registerRenderer(FrozenSharedConstants.id("goal"), improvedGoalSelectorRenderer::render);

		registerRenderer(FrozenSharedConstants.id("game_event"), improvedGameEventRenderer::render);



		addClearRunnable(windDebugRenderer::clear);

		registerRenderer(FrozenSharedConstants.id("wind"), windDebugRenderer::render);

		addClearRunnable(windDisturbanceDebugRenderer::clear);

		registerRenderer(FrozenSharedConstants.id("wind_disturbance"), windDisturbanceDebugRenderer::render);
	}

	@SubscribeEvent
	public static void startWorldTick(ClientTickEvent.Start event) {
		if (FrozenLibConfig.IS_DEBUG) {
			improvedGameEventRenderer.tick();
			windDebugRenderer.tick();
			ClientWindManager.clearAccessedPositions();
			windDisturbanceDebugRenderer.tick();
		}
	}

	public static void clearAdditionalRenderers() {
		ON_CLEAR_RUNNABLES.forEach(Runnable::run);
	}

	public static void addClearRunnable(Runnable runnable) {
		ON_CLEAR_RUNNABLES.add(runnable);
	}

	public static float PARTIAL_TICK;

	public static void updatePartialTick() {
		PARTIAL_TICK = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
	}
}
