package net.frozenblock.lib.loot.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.lib.loot.api.FrozenLootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.level.storage.loot.LootDataType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SimpleJsonResourceReloadListener.class)
public class SimpleJsonResourceReloadListenerMixin {
    @Inject(
            method = "scanDirectory",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/resources/FileToIdConverter;fileToId(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceLocation;",
                    shift = At.Shift.AFTER
            )
    )
    private static void inject(ResourceManager resourceManager, String name, Gson gson, Map<ResourceLocation, JsonElement> output, CallbackInfo ci, @Local Map.Entry<ResourceLocation, Resource> entry, @Local(ordinal = 1) ResourceLocation id) {
        if(LootDataType.TABLE.registryKey().location().getPath().equals(name))
            FrozenLootTableSource.SOURCES.get().put(id, FrozenLootTableSource.determineSource(entry.getValue()));
    }
}
