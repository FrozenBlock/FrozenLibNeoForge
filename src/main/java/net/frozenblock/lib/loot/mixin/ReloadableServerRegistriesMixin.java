package net.frozenblock.lib.loot.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.DynamicOps;
import net.frozenblock.lib.loot.api.ApplyInterface;
import net.frozenblock.lib.loot.api.FrozenLootTableSource;
import net.frozenblock.lib.loot.api.LootTableModifyEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(ReloadableServerRegistries.class)
public class ReloadableServerRegistriesMixin {

    @Unique
    private static final WeakHashMap<RegistryOps<JsonElement>, HolderLookup.Provider> frozenLibNeoForge$weakMap = new WeakHashMap<>();

    @WrapOperation(
            method = "reload",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/ReloadableServerRegistries$EmptyTagLookupWrapper;createSerializationContext(Lcom/mojang/serialization/DynamicOps;)Lnet/minecraft/resources/RegistryOps;"
            )
    )
    private static RegistryOps<JsonElement> saveOps(ReloadableServerRegistries.EmptyTagLookupWrapper instance, DynamicOps<JsonElement> dynamicOps, Operation<RegistryOps<JsonElement>> original) {
        @SuppressWarnings("all")
        RegistryOps<JsonElement> created = original.call(instance, dynamicOps);
        frozenLibNeoForge$weakMap.put(created, instance);
        return created;
    }

    @WrapOperation(
            method = "reload",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;thenApplyAsync(Ljava/util/function/Function;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private static CompletableFuture<LayeredRegistryAccess<RegistryLayer>> remove(CompletableFuture<List<WritableRegistry<?>>> instance, Function<? super List<WritableRegistry<?>>, ? extends LayeredRegistryAccess<RegistryLayer>> fn, Executor executor, Operation<CompletableFuture<LayeredRegistryAccess<RegistryLayer>>> original, @Local RegistryOps<JsonElement> ops) {
        return original.call(instance.thenApply(v -> {
            frozenLibNeoForge$weakMap.remove(ops);
            return v;
        }), fn, executor);
    }

    @WrapOperation(
            method = "lambda$scheduleElementParse$3",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"
            )
    )
    private static <T> void modify(@SuppressWarnings("all") Optional<T> optionalTable, Consumer<? super T> action, Operation<Void> original, @Local(argsOnly = true) ResourceLocation id, @Local(argsOnly = true) RegistryOps<JsonElement> ops) {
        original.call(optionalTable.map(table -> frozenLibNeoForge$modifyLootTable(table, id, ops)), action);
    }

    @SuppressWarnings("unchecked")
    @Unique
    private static <T> T frozenLibNeoForge$modifyLootTable(T value, ResourceLocation id, RegistryOps<JsonElement> ops) {
        if(value instanceof LootTable table) {
            if(table == LootTable.EMPTY) {
                return value;
            } else {
                ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, id);
                HolderLookup.Provider registries = frozenLibNeoForge$weakMap.get(ops);
                LootTable.Builder builder = frozenLibNeoForge$createBuilder(table);
                NeoForge.EVENT_BUS.post(new LootTableModifyEvent(key, builder, registries, FrozenLootTableSource.SOURCES.get().getOrDefault(id, false)));
                return (T) builder.build();
            }
        } else {
            return value;
        }
    }

    @Unique
    private static LootTable.Builder frozenLibNeoForge$createBuilder(LootTable table) {
        LootTable.Builder builder = LootTable.lootTable();
        LootTableAccessor accessor = (LootTableAccessor)table;
        builder.setParamSet(table.getParamSet());
        ((ApplyInterface)builder).pools(accessor.getPools());
        ((ApplyInterface)builder).apply(accessor.getFunctions());
        Optional<ResourceLocation> idk = accessor.getRandomSequence();
        Objects.requireNonNull(builder);
        idk.ifPresent(builder::setRandomSequence);
        return builder;
    }

}
