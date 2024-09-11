package net.frozenblock.lib.loot.api;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.Collection;

public interface ApplyInterface {
    LootTable.Builder pools(Collection<? extends LootPool> pools);
    LootTable.Builder apply(Collection<? extends LootItemFunction> functions);
}
