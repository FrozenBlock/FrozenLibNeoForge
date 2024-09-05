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

package net.frozenblock.lib.worldgen.feature.api.placementmodifier;

import com.mojang.serialization.MapCodec;
import net.frozenblock.lib.FrozenSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

public class FrozenPlacementModifiers {
	public static final PlacementModifierType<LowerHeightmapPlacement> ACCURATE_HEIGHTMAP = () -> LowerHeightmapPlacement.CODEC;
	public static final PlacementModifierType<NoisePlacementFilter> NOISE_FILTER = () -> NoisePlacementFilter.CODEC;

	public static void init(RegisterEvent.RegisterHelper<PlacementModifierType<?>> registry) {
		registry.register(FrozenSharedConstants.id("improved_heightmap"), ACCURATE_HEIGHTMAP);
		registry.register(FrozenSharedConstants.id("noise_filter"), NOISE_FILTER);
	}
}
