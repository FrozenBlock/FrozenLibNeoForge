package net.frozenblock.lib.block.api;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public interface LootTableInjection {
    ResourceKey<LootTable> getLootTableForInject();
}
