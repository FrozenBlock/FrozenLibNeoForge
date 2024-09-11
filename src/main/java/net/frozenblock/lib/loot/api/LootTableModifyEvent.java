package net.frozenblock.lib.loot.api;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.Event;

public class LootTableModifyEvent extends Event {
    public final ResourceKey<LootTable> key;
    public final HolderLookup.Provider registryLookup;
    public final LootTable.Builder builder;
    public final boolean isBuiltin;

    public LootTableModifyEvent(final ResourceKey<LootTable> key, LootTable.Builder builder, final HolderLookup.Provider registryLookup, final boolean isBuiltin) {
        this.key = key;
        this.registryLookup = registryLookup;
        this.builder = builder;
        this.isBuiltin = isBuiltin;
    }
}
