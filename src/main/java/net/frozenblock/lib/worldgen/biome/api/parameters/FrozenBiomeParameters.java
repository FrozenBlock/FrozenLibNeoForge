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

package net.frozenblock.lib.worldgen.biome.api.parameters;

import lombok.experimental.UtilityClass;
import net.minecraft.world.level.biome.Climate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class FrozenBiomeParameters {

	public static void addWeirdness(BiomeRunnable runnable, @NotNull List<Climate.Parameter> weirdnesses) {
		for (Climate.Parameter weirdness : weirdnesses) {
			runnable.run(weirdness);
		}
	}

	/**
	 * Returns climate parameters in between both specified parameters.
	 * <p>
	 * Is NOT identical to Climate.Parameter;span.
	 * Instead, this will meet in the middle of both parameters.
	 */
	@NotNull
	public static Climate.Parameter inBetween(Climate.Parameter par1, Climate.Parameter par2, float width) {
		if (width >= 1F || width <= 0F) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetween if width >= 1 or width <= 0!");
		}
		width *= 0.5F;
		float lowest = par1.min();
		float highest = par2.max();
		if (lowest > highest) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetween when lower parameter is higher than the first!");
		}
		float difference = (highest - lowest);
		float middle = lowest + (difference * 0.5F);
		float offset = difference * width;
		return Climate.Parameter.span(middle - offset, middle + offset);
	}

	@NotNull
	public static Climate.Parameter inBetweenLowCutoff(Climate.Parameter par1, Climate.Parameter par2, float width) {
		if (width >= 1F || width <= 0F) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenLowCutoff if width >= 1 or width <= 0!");
		}
		width *= 0.5F;
		float lowest = par1.min();
		float highest = par2.max();
		if (lowest > highest) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenLowCutoff when lower parameter is higher than the first!");
		}
		float difference = (highest - lowest);
		float middle = lowest + (difference * 0.5F);
		float offset = difference * width;
		return Climate.Parameter.span(lowest, middle - offset);
	}

	@NotNull
	public static Climate.Parameter inBetweenHighCutoff(Climate.Parameter par1, Climate.Parameter par2, float width) {
		if (width >= 1F || width <= 0F) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenHighCutoff if width >= 1 or width <= 0!");
		}
		width *= 0.5F;
		float lowest = par1.min();
		float highest = par2.max();
		if (lowest > highest) {
			throw new IllegalArgumentException("FrozenLib: Cannot run inBetweenHighCutoff when lower parameter is higher than the first!");
		}
		float difference = (highest - lowest);
		float middle = lowest + (difference * 0.5F);
		float offset = difference * width;
		return Climate.Parameter.span(middle + offset, highest);
	}

	@NotNull
	@Contract("_, _ -> new")
	public static Climate.Parameter squish(@NotNull Climate.Parameter parameter, float squish) {
		return Climate.Parameter.span(parameter.min() + squish, parameter.max() - squish);
	}

	public static boolean isWeird(@NotNull Climate.ParameterPoint point) {
		return point.weirdness().max() < 0L;
	}

	@FunctionalInterface
	public interface BiomeRunnable {
		void run(Climate.Parameter weirdness);
	}
}
