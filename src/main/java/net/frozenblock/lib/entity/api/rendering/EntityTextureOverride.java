package net.frozenblock.lib.entity.api.rendering;

import net.frozenblock.lib.registry.api.client.FrozenClientRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


@OnlyIn(Dist.CLIENT)
public record EntityTextureOverride<T extends LivingEntity>(EntityType<T> type, ResourceLocation texture, Condition<T> condition) {
    public static <T extends LivingEntity> EntityTextureOverride<T> register(ResourceLocation key, EntityType<T> type, ResourceLocation texture, String... names) {
        return register(key, type, texture, false, names);
    }

    public static <T extends LivingEntity> EntityTextureOverride<T> register(ResourceLocation key, EntityType<T> type, ResourceLocation texture, boolean caseSensitive, String... names) {
        return register(key, type, texture, entity -> {
            String entityName = ChatFormatting.stripFormatting(entity.getName().getString());
            AtomicBoolean isNameCorrect = new AtomicBoolean(false);
            if (names.length == 0) {
                return true;
            } else {
                Arrays.stream(names).toList().forEach(name -> {
                    if (entityName != null) {
                        if (caseSensitive) {
                            if (entityName.equalsIgnoreCase(name)) {
                                isNameCorrect.set(true);
                            }
                        } else {
                            if (entityName.equals(name)) {
                                isNameCorrect.set(true);
                            }
                        }
                    }
                });
            }
            return isNameCorrect.get();
        });
    }

    public static <T extends LivingEntity> EntityTextureOverride<T> register(ResourceLocation key, EntityType<T> type, ResourceLocation texture, Condition<T> condition) {
        return Registry.register(FrozenClientRegistry.ENTITY_TEXTURE_OVERRIDES, key, new EntityTextureOverride<>(type, texture, condition));
    }


    @OnlyIn(Dist.CLIENT)
    @FunctionalInterface
    public interface Condition<T extends LivingEntity> {
        boolean condition(T entity);
    }
}
