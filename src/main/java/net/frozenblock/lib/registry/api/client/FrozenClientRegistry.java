package net.frozenblock.lib.registry.api.client;

import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.entity.api.rendering.EntityTextureOverride;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FrozenSharedConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrozenClientRegistry {

    public static final ResourceKey<Registry<EntityTextureOverride<?>>> ENTITY_TEXTURE_OVERRIDE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("yourmodid", "spells"));
    public static final Registry<EntityTextureOverride<?>> ENTITY_TEXTURE_OVERRIDES = new RegistryBuilder<>(ENTITY_TEXTURE_OVERRIDE_KEY)
            .sync(false)
            .create();

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(ENTITY_TEXTURE_OVERRIDES);
    }
}
