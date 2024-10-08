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

package net.frozenblock.lib.ingamedevtools;

import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.ingamedevtools.item.Camera;
import net.frozenblock.lib.ingamedevtools.item.LootTableWhacker;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;

public class RegisterInGameDevTools {
	public static final Item CAMERA = new Camera(new Item.Properties().stacksTo(1));
	public static final Item LOOT_TABLE_WHACKER = new LootTableWhacker(new Item.Properties().stacksTo(1));

	public static void register(RegisterEvent.RegisterHelper<Item> registry) {
		registry.register(FrozenSharedConstants.id("camera"), CAMERA);
		registry.register(ResourceLocation.parse(FrozenSharedConstants.string("loot_table_whacker")), LOOT_TABLE_WHACKER);
	}
}
