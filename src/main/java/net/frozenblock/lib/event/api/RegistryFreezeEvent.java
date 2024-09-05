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

package net.frozenblock.lib.event.api;

import net.minecraft.core.Registry;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

/**
 * {@link Registry} freeze events
 */
public class RegistryFreezeEvent extends Event {

	@Nullable public final Registry<?> registry;
	public final boolean allRegistries;

	private RegistryFreezeEvent(@Nullable Registry<?> registry, boolean allRegistries) {
		this.registry = registry;
		this.allRegistries = allRegistries;
	}

	/**
	 * Invoked when a {@link Registry} is about to be frozen
	 * <p>
	 * The registry will not be frozen when this is invoked.
	 */
	public static class Start extends RegistryFreezeEvent {

		public Start(@Nullable Registry<?> registry, boolean allRegistries) {
			super(registry, allRegistries);
		}
	}

	/**
	 * Invoked when a {@link Registry} is already frozen
	 * <p>
	 * The registry will be frozen when this is invoked.
	 */
	public static class End extends RegistryFreezeEvent {

		public End(@Nullable Registry<?> registry, boolean allRegistries) {
			super(registry, allRegistries);
		}
	}

}
