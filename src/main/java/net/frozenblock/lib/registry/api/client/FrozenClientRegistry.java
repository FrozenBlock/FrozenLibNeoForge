package net.frozenblock.lib.registry.api.client;

import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.entity.api.rendering.EntityTextureOverride;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FrozenSharedConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrozenClientRegistry {

    public static final ResourceKey<Registry<EntityTextureOverride>> ENTITY_TEXTURE_OVERRIDE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("yourmodid", "spells"));
    public static final Registry<EntityTextureOverride> ENTITY_TEXTURE_OVERRIDE = new RegistryBuilder<>(ENTITY_TEXTURE_OVERRIDE_KEY)
            .sync(false)
            .create();

    @SubscribeEvent
    public static void registerRegistries(final NewRegistryEvent event) {
        event.register(ENTITY_TEXTURE_OVERRIDE);
    }
}
