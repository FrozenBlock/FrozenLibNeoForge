package net.frozenblock.lib;

import net.frozenblock.lib.core.impl.DataPackReloadMarker;
import net.frozenblock.lib.entrypoint.api.FrozenMainEntrypoint;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;
import net.frozenblock.lib.gravity.api.GravityAPI;
import net.frozenblock.lib.ingamedevtools.RegisterInGameDevTools;
import net.frozenblock.lib.networking.FrozenNetworking;
import net.frozenblock.lib.particle.api.FrozenParticleTypes;
import net.frozenblock.lib.registry.api.FrozenRegistry;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.frozenblock.lib.spotting_icons.api.SpottingIconPredicate;
import net.frozenblock.lib.tag.api.TagKeyArgument;
import net.frozenblock.lib.wind.api.WindDisturbanceLogic;
import net.frozenblock.lib.worldgen.feature.api.FrozenFeatures;
import net.frozenblock.lib.worldgen.feature.api.placementmodifier.FrozenPlacementModifiers;
import net.frozenblock.lib.worldgen.structure.impl.FrozenRuleBlockEntityModifiers;
import net.frozenblock.lib.worldgen.structure.impl.FrozenStructureProcessorTypes;
import net.frozenblock.lib.worldgen.surface.impl.BiomeTagConditionSource;
import net.frozenblock.lib.worldgen.surface.impl.OptimizedBiomeTagConditionSource;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.quiltmc.qsl.frozenblock.core.registry.api.sync.ModProtocol;
import org.quiltmc.qsl.frozenblock.core.registry.impl.sync.server.ServerRegistrySync;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.impl.ServerFreezer;


@Mod(FrozenSharedConstants.MOD_ID)
public class FrozenMain extends FrozenModInitializer {

    public FrozenMain(IEventBus modEventBus, ModContainer modContainer) {
        super(FrozenSharedConstants.MOD_ID, modEventBus, modContainer, FrozenEventListener.class);
    }

    @Override
    public void onInitialize(String modId, IEventBus eventBus, ModContainer container) {
        FrozenRegistry.initRegistry();

        // QUILT INIT

        ServerFreezer.onInitialize();
        ModProtocol.loadVersions();
        ServerRegistrySync.registerHandlers();

        // We call the entrypoint, which on forge is an event
        NeoForge.EVENT_BUS.post(new FrozenMainEntrypoint());

        // Extra Listeners
        NeoForge.EVENT_BUS.register(DataPackReloadMarker.class);
        eventBus.register(FrozenNetworking.class);
        NeoForge.EVENT_BUS.register(GravityAPI.class);
        eventBus.register(FrozenRegistry.class);
    }

    @SubscribeEvent
    public void registerEvent(RegisterEvent event) {
        event.register(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER.key(), FrozenRuleBlockEntityModifiers::init);
        event.register(BuiltInRegistries.ITEM.key(), RegisterInGameDevTools::register);
        event.register(BuiltInRegistries.STRUCTURE_PROCESSOR.key(), FrozenStructureProcessorTypes::init);
        event.register(FrozenRegistry.SOUND_PREDICATE.key(), SoundPredicate::init);
        event.register(FrozenRegistry.SPOTTING_ICON_PREDICATE.key(), SpottingIconPredicate::init);
        event.register(FrozenRegistry.WIND_DISTURBANCE_LOGIC.key(), WindDisturbanceLogic::init);
        event.register(BuiltInRegistries.FEATURE.key(), FrozenFeatures::init);
        event.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.key(), FrozenPlacementModifiers::init);
        event.register(BuiltInRegistries.MATERIAL_CONDITION.key(), registry -> {
            registry.register(FrozenSharedConstants.id("biome_tag_condition_source"), BiomeTagConditionSource.CODEC.codec());
            registry.register(FrozenSharedConstants.id("optimized_biome_tag_condition_source"), OptimizedBiomeTagConditionSource.CODEC.codec());
        });
        event.register(BuiltInRegistries.PARTICLE_TYPE.key(), FrozenParticleTypes::registerParticles);
        event.register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE.key(), registry -> {
            ArgumentTypeInfos.register(
                    BuiltInRegistries.COMMAND_ARGUMENT_TYPE,
                    FrozenSharedConstants.string("tag_key"),
                    ArgumentTypeInfos.fixClassType(TagKeyArgument.class),
                    new TagKeyArgument.Info<>()
            );
        });
    }
}
