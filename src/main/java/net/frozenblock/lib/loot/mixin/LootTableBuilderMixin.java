package net.frozenblock.lib.loot.mixin;

import com.google.common.collect.ImmutableList;
import net.frozenblock.lib.loot.api.ApplyInterface;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

@Mixin(LootTable.Builder.class)
public class LootTableBuilderMixin implements ApplyInterface {

    @Shadow @Final private ImmutableList.Builder<LootItemFunction> functions;

    @Shadow @Final private ImmutableList.Builder<LootPool> pools;

    @Override
    public LootTable.Builder pools(Collection<? extends LootPool> pools) {
        this.pools.addAll(pools);
        return (LootTable.Builder)(Object)this;
    }

    @Override
    public LootTable.Builder apply(Collection<? extends LootItemFunction> functions) {
        this.functions.addAll(functions);
        return (LootTable.Builder)(Object)this;
    }
}
