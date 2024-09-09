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

package org.quiltmc.qsl.frozenblock.core.registry.api.sync;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntList;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import java.util.*;

public class ModProtocol {
	public static boolean enabled = false;
	public static boolean disableQuery = false;
	public static String prioritizedId = "";
	public static ModProtocolDef prioritizedEntry;
	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<String, ModProtocolDef> PROTOCOL_VERSIONS = new HashMap<>();
	public static final List<ModProtocolDef> REQUIRED = new ArrayList<>();
	public static final List<ModProtocolDef> ALL = new ArrayList<>();

	@SuppressWarnings("ConstantConditions")
	public static void loadVersions() {
		NeoForge.EVENT_BUS.post(new LoadModProtocolEvent());
		for(var modInfo : ModList.get().getMods()) {
			String id = modInfo.getModId();
			try {

				var entry = ENTRIES.get(id);
				if (entry == null)
					continue;
				var object = entry.get("mod_protocol");
				final IntList version = IntList.of();
				boolean optional = false;
				if (object.isJsonArray()) {
					for (var in : object.getAsJsonArray()) {
						version.add(in.getAsInt());
					}
				} else {
					try {
						int versionN = object.getAsInt();
						if (versionN < 0) negativeEntry(id, modInfo.getDisplayName(), versionN);
						version.add(versionN);
					} catch (NumberFormatException exception) {
						final JsonElement o = object.getAsJsonObject().get("value");
						if(o.isJsonArray()) {
							for(var in : o.getAsJsonArray()) {
								version.add(in.getAsInt());
							}
						} else {
							version.add(o.getAsInt());
						}
						optional = object.getAsJsonObject().get("optional").getAsBoolean();
					}
				}
				add(new ModProtocolDef(id, modInfo.getDisplayName(), version, optional));
			} catch (Exception e) {
				invalidEntryType(id, modInfo.getDisplayName());
				LOGGER.warn(Arrays.toString(e.getStackTrace()));
			}

		}
	}

	private static void invalidEntryType(String path, String displayName) {
		LOGGER.warn("Mod {} ({}) contains invalid FrozenLibRegistry-ModProtocol entry!", path, displayName);
	}

	private static void negativeEntry(String path, String displayName, int i) {
		LOGGER.warn("Mod {} ({}) contains invalid FrozenLibRegistry-ModProtocol entry! Protocol version must be positive or 0 (found {})", path, displayName, i);
	}

	public static IntList getVersion(String string) {
		var x = PROTOCOL_VERSIONS.get(string);
		return x == null ? IntList.of() : x.versions();
	}

	public static void add(ModProtocolDef def) {
		PROTOCOL_VERSIONS.put(def.id(), def);

		if (!def.optional()) {
			REQUIRED.add(def);
		}

		ALL.add(def);
		enabled = true;
	}

	private static HashMap<String, ModProtocolEntry> ENTRIES = new HashMap<>();

	public static class LoadModProtocolEvent extends Event {
		public void register(String modId, ModProtocolEntry entry) throws IllegalAccessException {
			if(ENTRIES.containsKey(modId)) throw new IllegalAccessException("Unable to register two entries under the same id (" + modId + ")");
			ENTRIES.put(modId, entry);
		}

	}

	public static class ModProtocolEntry {
		private final JsonObject object = new JsonObject();

		public ModProtocolEntry(final int version) {
			object.addProperty("mod_protocol", version);
		}

		public ModProtocolEntry(final IntList versions) {
			final JsonArray array = new JsonArray();
			versions.forEach(array::add);
			object.add("mod_protocol", array);
		}

		public ModProtocolEntry(final int version, boolean optional) {
			final JsonObject obj = new JsonObject();
			obj.addProperty("value", version);
			obj.addProperty("optional", optional);
			object.add("mod_protocol", obj);
		}

		public ModProtocolEntry(final IntList versions, boolean optional) {
			final JsonArray array = new JsonArray();
			versions.forEach(array::add);
			final JsonObject obj = new JsonObject();
			obj.add("value", array);
			obj.addProperty("optional", optional);
			object.add("mod_protocol", obj);
		}

		public JsonElement get(String value) {
			return object.get(value);
		}
	}

}
