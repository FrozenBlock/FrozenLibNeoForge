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

package org.quiltmc.qsl.frozenblock.core.registry.api.event;

import lombok.experimental.UtilityClass;
import net.minecraft.core.RegistryAccess;
import net.neoforged.bus.api.Event;

/**
 * Events for listening to the manipulation of Minecraft's content registries.
 * <p>
 * The events are to be used for very low-level purposes, and callbacks are only called on registry manipulations
 * occurring after the event registration. This means that mod load order can affect what is picked up by these events.
 * <p>
 * For more high-level monitoring of registries, including methods to ease the inconvenience of mod load order,
 * use RegistryMonitor.
 * <p>
 * Modified to work on Fabric
 */
@UtilityClass
public class RegistryEvent {

	/**
	 * This event gets triggered when a new {@link RegistryAccess} gets created,
	 * but before it gets filled.
	 * <p>
	 * This event can be used to register callbacks to dynamic registries, or to pre-fill some values.
	 * <p>
	 * <strong>Important Note</strong>: The passed dynamic registry manager might not
	 * contain the registry, as this event is invoked for each layer of
	 * the combined registry manager, and each layer holds different registries.
	 * Use {@link RegistryAccess#registry} to prevent crashes.
	 */
	public static class DynamicRegistrySetup extends Event {
		public final DynamicRegistryManagerSetupContext context;

		public DynamicRegistrySetup(DynamicRegistryManagerSetupContext context) {
			this.context = context;
		}
	}

	/**
	 * This event gets triggered when a new {@link RegistryAccess} gets created,
	 * after it has been filled with the registry entries specified by data packs.
	 * <p>
	 * This event can be used to register callbacks to dynamic registries, or to inspect values.
	 * <p>
	 * <strong>Important Note</strong>: The passed dynamic registry manager might not
	 * contain the registry, as this event is invoked for each layer of
	 * the combined registry manager, and each layer holds different registries.
	 * Use {@link RegistryAccess#registry} to prevent crashes.
	 */

	public static class DynamicRegistryLoaded extends Event {
		public final RegistryAccess registryAccess;

		public DynamicRegistryLoaded(final RegistryAccess access) {
			registryAccess = access;
		}
	}
}
