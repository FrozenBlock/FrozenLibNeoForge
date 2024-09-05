package net.frozenblock.lib.registry.api;

import com.mojang.serialization.Lifecycle;
import lombok.experimental.UtilityClass;
import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.integration.api.ModIntegration;
import net.frozenblock.lib.integration.api.ModIntegrationSupplier;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.frozenblock.lib.spotting_icons.api.SpottingIconPredicate;
import net.frozenblock.lib.wind.api.WindDisturbanceLogic;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class FrozenRegistry {
    public static final ResourceKey<Registry<ModIntegrationSupplier<?>>> MOD_INTEGRATION_REGISTRY = ResourceKey.createRegistryKey(FrozenSharedConstants.id("mod_integration"));
    public static final ResourceKey<Registry<SoundPredicate<?>>> SOUND_PREDICATE_REGISTRY = ResourceKey.createRegistryKey(FrozenSharedConstants.id("sound_predicate"));
    public static final ResourceKey<Registry<SoundPredicate<?>>> SOUND_PREDICATE_UNSYNCED_REGISTRY = ResourceKey.createRegistryKey(FrozenSharedConstants.id("sound_predicate_unsynced"));
    public static final ResourceKey<Registry<SpottingIconPredicate<?>>> SPOTTING_ICON_PREDICATE_REGISTRY = ResourceKey.createRegistryKey(FrozenSharedConstants.id("spotting_icon_predicate"));
    public static final ResourceKey<Registry<WindDisturbanceLogic<?>>> WIND_DISTURBANCE_LOGIC_REGISTRY = ResourceKey.createRegistryKey(FrozenSharedConstants.id("wind_disturbance_logic"));
    public static final ResourceKey<Registry<WindDisturbanceLogic<?>>> WIND_DISTURBANCE_LOGIC_UNSYNCED_REGISTRY = ResourceKey.createRegistryKey(FrozenSharedConstants.id("wind_disturbance_logic_unsynced"));

    public static final MappedRegistry<ModIntegrationSupplier<?>> MOD_INTEGRATION = createSimple(MOD_INTEGRATION_REGISTRY, null,
            registry -> Registry.register(registry, FrozenSharedConstants.id("dummy"), new ModIntegrationSupplier<>(() -> new ModIntegration("dummy") {
                @Override
                public void init() {}
            },
                    "dummy"
            ))
    );

    public static final MappedRegistry<SoundPredicate<?>> SOUND_PREDICATE = createSimple(SOUND_PREDICATE_REGISTRY, true,
            registry -> Registry.register(registry, FrozenSharedConstants.id("dummy"), new SoundPredicate<>(() -> entity -> false))
    );

    public static final MappedRegistry<SoundPredicate<?>> SOUND_PREDICATE_UNSYNCED = createSimple(SOUND_PREDICATE_UNSYNCED_REGISTRY, null,
            registry -> Registry.register(registry, FrozenSharedConstants.id("dummy"), new SoundPredicate<>(() -> entity -> false))
    );

    public static final MappedRegistry<SpottingIconPredicate<?>> SPOTTING_ICON_PREDICATE = createSimple(SPOTTING_ICON_PREDICATE_REGISTRY, true,
            registry -> Registry.register(registry, FrozenSharedConstants.id("dummy"), new SpottingIconPredicate<>(entity -> false))
    );

    public static final MappedRegistry<WindDisturbanceLogic<?>> WIND_DISTURBANCE_LOGIC = createSimple(WIND_DISTURBANCE_LOGIC_REGISTRY, true,
            registry -> Registry.register(registry, FrozenSharedConstants.id("dummy"), new WindDisturbanceLogic(WindDisturbanceLogic.defaultPredicate()))
    );

    public static final MappedRegistry<WindDisturbanceLogic<?>> WIND_DISTURBANCE_LOGIC_UNSYNCED = createSimple(WIND_DISTURBANCE_LOGIC_UNSYNCED_REGISTRY, null,
            registry -> Registry.register(registry, FrozenSharedConstants.id("dummy"), new WindDisturbanceLogic(WindDisturbanceLogic.defaultPredicate()))
    );

    @NotNull
    public static HolderLookup.Provider vanillaRegistries() {
        return VanillaRegistries.createLookup();
    }

    public static void initRegistry() {
    }

    private static <T> MappedRegistry<T> createSimple(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        return createSimple(key, lifecycle, null);
    }

    private static <T> MappedRegistry<T> createSimple(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, Boolean attribute) {
        return createSimple(key, attribute, null);
    }

    private static <T> MappedRegistry<T> createSimple(ResourceKey<? extends Registry<T>> key, Boolean attribute, BuiltInRegistries.RegistryBootstrap<T> bootstrap) {
        RegistryBuilder<T> registryBuilder = new RegistryBuilder<>(key);

        if (attribute != null) {
            registryBuilder.sync(attribute);
        }

        final var registry = registryBuilder.create();

        if (bootstrap != null) {
            bootstrap.run(registry);
        }

        return (MappedRegistry<T>) registry;
    }

    @SubscribeEvent
    public static void registerRegistries(final NewRegistryEvent event) {
        event.register(MOD_INTEGRATION);
        event.register(SOUND_PREDICATE);
        event.register(SOUND_PREDICATE_UNSYNCED);
        event.register(SPOTTING_ICON_PREDICATE);
        event.register(WIND_DISTURBANCE_LOGIC);
        event.register(WIND_DISTURBANCE_LOGIC_UNSYNCED);
    }
}
