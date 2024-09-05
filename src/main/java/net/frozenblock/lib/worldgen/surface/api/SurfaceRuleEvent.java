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

package net.frozenblock.lib.worldgen.surface.api;

import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Events that allows adding surface rules to dimensions.
 * <p>
 * Defined with the {@code frozenlib:events} key in {@code fabric.mod.json}.
 * <p>
 * Compatible with TerraBlender.
 */
public class SurfaceRuleEvent<T> extends Event {
	public ArrayList<T> sourceHolders;

	private SurfaceRuleEvent(ArrayList<T> sourceHolders) {
		this.sourceHolders = sourceHolders;
	}

	/**
	 * Lets you modify the Surface Rules of Overworld-based world presets.
	 */
	public static class ModifyOverworld extends SurfaceRuleEvent<SurfaceRules.RuleSource> {
		public ModifyOverworld(ArrayList<SurfaceRules.RuleSource> sourceHolders) {
			super(sourceHolders);
		}
	}

	/**
	 * Lets you modify the Surface Rules of Overworld-based world presets without checking the preliminary surface.
	 */
	public static class ModifyOverworldNoPreliminarySurface extends SurfaceRuleEvent<SurfaceRules.RuleSource> {
		public ModifyOverworldNoPreliminarySurface(ArrayList<SurfaceRules.RuleSource> sourceHolders) {
			super(sourceHolders);
		}
	}

	/**
	 * Lets you modify the Surface Rules of Nether-based world presets.
	 */
	public static class ModifyNether extends SurfaceRuleEvent<SurfaceRules.RuleSource> {
		public ModifyNether(ArrayList<SurfaceRules.RuleSource> sourceHolders) {
			super(sourceHolders);
		}
	}

	/**
	 * Lets you modify the Surface Rules of End-based world presets.
	 */
	public static class ModifyEnd extends SurfaceRuleEvent<SurfaceRules.RuleSource> {
		public ModifyEnd(ArrayList<SurfaceRules.RuleSource> sourceHolders) {
			super(sourceHolders);
		}
	}

	/**
	 * Lets you modify the Surface Rules of custom world presets.
	 */
	public static class ModifyGeneric extends SurfaceRuleEvent<FrozenDimensionBoundRuleSource> {
		public ModifyGeneric(ArrayList<FrozenDimensionBoundRuleSource> sourceHolders) {
			super(sourceHolders);
		}
	}
}
