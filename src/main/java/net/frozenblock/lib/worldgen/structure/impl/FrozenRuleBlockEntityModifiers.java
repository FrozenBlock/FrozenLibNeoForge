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

package net.frozenblock.lib.worldgen.structure.impl;

import com.mojang.serialization.MapCodec;
import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.worldgen.structure.api.AppendSherds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

public class FrozenRuleBlockEntityModifiers {
	public static final RuleBlockEntityModifierType<AppendSherds> APPEND_SHERDS = () -> AppendSherds.CODEC;

	public static void init(RegisterEvent.RegisterHelper<RuleBlockEntityModifierType<?>> registry) {
		register(registry, "append_sherds", APPEND_SHERDS);
	}

	private static void register(RegisterEvent.RegisterHelper<RuleBlockEntityModifierType<?>> registry, String name, RuleBlockEntityModifierType<?> value) {
		registry.register(FrozenSharedConstants.id(name), value);
	}
}
