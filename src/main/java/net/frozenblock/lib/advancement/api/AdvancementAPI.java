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

package net.frozenblock.lib.advancement.api;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

public final class AdvancementAPI {
	private AdvancementAPI() {}

	/**
	 * Makes a copy of {@link AdvancementRewards#EMPTY} for use in the Advancement API
	 * <p>
	 * Use only when needed, as this will increase memory usage
	 */
	public static Advancement setupRewards(Advancement advancement) {
		AdvancementRewards rewards = advancement.rewards();
		if (advancement.rewards() == AdvancementRewards.EMPTY) {
			rewards = new AdvancementRewards(0, List.of(), List.of(), Optional.empty());
		}
		return new Advancement(advancement.parent(), advancement.display(), rewards, advancement.criteria(), advancement.requirements(), advancement.sendsTelemetryEvent(), advancement.name());
	}

	/**
	 * Makes a copy of {@link AdvancementRequirements#EMPTY} for use in the Advancement API
	 * <p>
	 * Use only when needed, as this will increase memory usage
	 */
	public static Advancement setupRequirements(Advancement advancement) {
		AdvancementRequirements requirements = advancement.requirements();
		if (advancement.requirements() == AdvancementRequirements.EMPTY) {
			requirements = new AdvancementRequirements(List.of());
		}
		return new Advancement(advancement.parent(), advancement.display(), advancement.rewards(), advancement.criteria(), requirements, advancement.sendsTelemetryEvent(), advancement.name());
	}

	public static Advancement setupCriteria(Advancement advancement) {
		Map<String, Criterion<?>> criteria = advancement.criteria();
		if (!(advancement.criteria() instanceof HashMap<String, Criterion<?>>)) {
			criteria = new HashMap<>(advancement.criteria());
		}
		return new Advancement(advancement.parent(), advancement.display(), advancement.rewards(), criteria, advancement.requirements(), advancement.sendsTelemetryEvent(), advancement.name());
	}

	public static void addCriteria(Advancement advancement, String key, Criterion<?> criterion) {
		if (criterion == null) return;
		setupCriteria(advancement);
		advancement.criteria().putIfAbsent(key, criterion);
	}

	public static Advancement addRequirementsAsNewList(Advancement advancement, AdvancementRequirements requirements) {
		if (requirements == null || requirements.isEmpty()) return advancement;
		setupRequirements(advancement);
		List<List<String>> list = new ArrayList<>(advancement.requirements().requirements());
		list.addAll(requirements.requirements());
		return new Advancement(advancement.parent(), advancement.display(), advancement.rewards(), advancement.criteria(), new AdvancementRequirements(Collections.unmodifiableList(list)), advancement.sendsTelemetryEvent(), advancement.name());
	}

	public static Advancement addRequirementsToList(Advancement advancement, List<String> requirements) {
		if (requirements == null || requirements.isEmpty()) return advancement;
		setupRequirements(advancement);
		List<List<String>> list = new ArrayList<>(advancement.requirements().requirements());
		if (list.isEmpty()) {
			list.add(requirements);
		} else {
			List<String> existingList = list.getFirst();
			List<String> finalList = new ArrayList<>(existingList);
			finalList.addAll(requirements);
			list.add(Collections.unmodifiableList(finalList));
			list.remove(existingList);
		}
		return new Advancement(advancement.parent(), advancement.display(), advancement.rewards(), advancement.criteria(), new AdvancementRequirements(Collections.unmodifiableList(list)), advancement.sendsTelemetryEvent(), advancement.name());
	}

	public static Advancement addLootTables(Advancement advancement, List<ResourceKey<LootTable>> lootTables) {
		if (lootTables.isEmpty()) return advancement;
		setupRewards(advancement);
		AdvancementRewards rewards = advancement.rewards();
		List<ResourceKey<LootTable>> newLoot = new ArrayList<>(rewards.loot());
		newLoot.addAll(lootTables);
		return new Advancement(advancement.parent(), advancement.display(), new AdvancementRewards(advancement.rewards().experience(), Collections.unmodifiableList(newLoot), advancement.rewards().recipes(), advancement.rewards().function()), advancement.criteria(), advancement.requirements(), advancement.sendsTelemetryEvent(), advancement.name());

	}

	public static Advancement addRecipes(Advancement advancement, List<ResourceLocation> recipes) {
		AdvancementRewards rewards = advancement.rewards();
		List<ResourceLocation> newLoot = new ArrayList<>(rewards.recipes());
		newLoot.addAll(recipes);
		return new Advancement(advancement.parent(), advancement.display(), new AdvancementRewards(advancement.rewards().experience(), advancement.rewards().loot(), Collections.unmodifiableList(newLoot), advancement.rewards().function()), advancement.criteria(), advancement.requirements(), advancement.sendsTelemetryEvent(), advancement.name());
	}
}
