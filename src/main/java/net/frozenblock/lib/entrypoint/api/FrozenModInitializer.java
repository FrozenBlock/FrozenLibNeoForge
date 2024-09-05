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

package net.frozenblock.lib.entrypoint.api;


import net.frozenblock.lib.FrozenSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

public abstract class FrozenModInitializer {

	private final String modId;
	private final ModContainer container;

	protected FrozenModInitializer(String modId, IEventBus modEventBus, ModContainer modContainer, Object eventListener) {
		this.modId = modId;
		this.container = modContainer;
		if(eventListener == null) {
			FrozenSharedConstants.LOGGER.warn("EventListener for mod " + modId + " is null");
		} else {
			NeoForge.EVENT_BUS.register(eventListener);
		}
		modEventBus.register(this);

		this.onInitialize(modId, modEventBus, modContainer);
	}

	public abstract void onInitialize(String modId, IEventBus eventBus, ModContainer container);


	public ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(this.modId, path);
	}

	public <T> T register(Registry<T> registry, String path, T value) {
		return Registry.register(registry, this.id(path), value);
	}
}
