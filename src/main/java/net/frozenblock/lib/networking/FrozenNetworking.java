package net.frozenblock.lib.networking;

import net.frozenblock.lib.config.impl.network.ConfigSyncPacket;
import net.frozenblock.lib.debug.client.impl.DebugRenderManager;
import net.frozenblock.lib.debug.networking.GoalDebugRemovePayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGameEventListenerDebugPayload;
import net.frozenblock.lib.debug.networking.ImprovedGoalDebugPayload;
import net.frozenblock.lib.item.impl.CooldownInterface;
import net.frozenblock.lib.item.impl.network.CooldownChangePacket;
import net.frozenblock.lib.item.impl.network.CooldownTickCountPacket;
import net.frozenblock.lib.item.impl.network.ForcedCooldownPacket;
import net.frozenblock.lib.screenshake.api.client.ScreenShaker;
import net.frozenblock.lib.screenshake.impl.network.EntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveEntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.ScreenShakePacket;
import net.frozenblock.lib.sound.api.instances.RestrictedMovingSound;
import net.frozenblock.lib.sound.api.instances.RestrictedMovingSoundLoop;
import net.frozenblock.lib.sound.api.instances.RestrictedStartingSound;
import net.frozenblock.lib.sound.api.instances.distance_based.FadingDistanceSwitchingSound;
import net.frozenblock.lib.sound.api.instances.distance_based.RestrictedMovingFadingDistanceSwitchingSound;
import net.frozenblock.lib.sound.api.instances.distance_based.RestrictedMovingFadingDistanceSwitchingSoundLoop;
import net.frozenblock.lib.sound.api.networking.*;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconPacket;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconRemovePacket;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.frozenblock.lib.wind.api.WindDisturbance;
import net.frozenblock.lib.wind.api.WindDisturbanceLogic;
import net.frozenblock.lib.wind.impl.networking.WindAccessPacket;
import net.frozenblock.lib.wind.impl.networking.WindDisturbancePacket;
import net.frozenblock.lib.wind.impl.networking.WindSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Optional;

public final class FrozenNetworking {
    private FrozenNetworking() {}

    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registry = event.registrar("1");

        // BI
        registry.playBidirectional(ConfigSyncPacket.PACKET_TYPE, ConfigSyncPacket.CODEC, new DirectionalPayloadHandler<>(
                (packet, ctx) -> ConfigSyncPacket.receive(packet, null),
                (packet, ctx) -> {
                    if (ConfigSyncPacket.hasPermissionsToSendSync(ctx.player(), true))
                        ConfigSyncPacket.receive(packet, ctx.player().getServer());
                }
        ));


        // S2C
        registry.playToClient(LocalPlayerSoundPacket.PACKET_TYPE, LocalPlayerSoundPacket.CODEC, (packet, ctx) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            Minecraft.getInstance().getSoundManager().play(new EntityBoundSoundInstance(packet.sound().value(), SoundSource.PLAYERS, packet.volume(), packet.pitch(), player, Minecraft.getInstance().level.random.nextLong()));
        });
        registry.playToClient(LocalSoundPacket.PACKET_TYPE, LocalSoundPacket.CODEC, (packet, ctx) -> {
            ClientLevel level = Minecraft.getInstance().level;
            Vec3 pos = packet.pos();
            level.playLocalSound(pos.x, pos.y, pos.z, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), packet.distanceDelay());
        });
        registry.playToClient(StartingMovingRestrictionSoundLoopPacket.PACKET_TYPE, StartingMovingRestrictionSoundLoopPacket.CODEC, (packet, ctx) -> {
            ClientLevel level = Minecraft.getInstance().level;
            Entity entity = level.getEntity(packet.id());
            if (entity != null) {
                SoundPredicate.LoopPredicate<Entity> predicate = SoundPredicate.getPredicate(packet.predicateId());
                Minecraft.getInstance().getSoundManager().play(new RestrictedStartingSound<>(
                        entity, packet.startingSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(),
                        new RestrictedMovingSoundLoop<>(
                                entity, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath()
                        )
                ));
            }
        });
        registry.playToClient(MovingRestrictionSoundPacket.PACKET_TYPE, MovingRestrictionSoundPacket.CODEC, (packet, ctx) -> {
            ClientLevel level = Minecraft.getInstance().level;
            Entity entity = level.getEntity(packet.id());
            if (entity != null) {
                SoundPredicate.LoopPredicate<Entity> predicate = SoundPredicate.getPredicate(packet.predicateId());
                if (packet.looping())
                    Minecraft.getInstance().getSoundManager().play(new RestrictedMovingSoundLoop<>(entity, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath()));
                else
                    Minecraft.getInstance().getSoundManager().play(new RestrictedMovingSound<>(entity, packet.sound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath()));
            }
        });
        registry.playToClient(FadingDistanceSwitchingSoundPacket.PACKET_TYPE, FadingDistanceSwitchingSoundPacket.CODEC, (packet, ctx) -> {
            Minecraft.getInstance().getSoundManager().play(new FadingDistanceSwitchingSound(packet.closeSound().value(), packet.category(), packet.volume(), packet.pitch(), packet.fadeDist(), packet.maxDist(), packet.volume(), false, packet.pos()));
            Minecraft.getInstance().getSoundManager().play(new FadingDistanceSwitchingSound(packet.farSound().value(), packet.category(), packet.volume(), packet.pitch(), packet.fadeDist(), packet.maxDist(), packet.volume(), true, packet.pos()));
        });
        registry.playToClient(MovingFadingDistanceSwitchingRestrictionSoundPacket.PACKET_TYPE, MovingFadingDistanceSwitchingRestrictionSoundPacket.CODEC, (packet, ctx) -> {
            SoundManager soundManager = Minecraft.getInstance().getSoundManager();
            ClientLevel level = Minecraft.getInstance().level;
            Entity entity =  level.getEntity(packet.id());
            if (entity != null) {
                SoundPredicate.LoopPredicate<Entity> predicate = SoundPredicate.getPredicate(packet.predicateId());
                if (packet.looping()) {
                    soundManager.play(new RestrictedMovingFadingDistanceSwitchingSoundLoop<>(entity, packet.closeSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(), packet.fadeDist(), packet.maxDist(), packet.volume(), false));
                    soundManager.play(new RestrictedMovingFadingDistanceSwitchingSoundLoop<>(entity, packet.farSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(), packet.fadeDist(), packet.maxDist(), packet.volume(), true));
                } else {
                    soundManager.play(new RestrictedMovingFadingDistanceSwitchingSound<>(entity, packet.closeSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(), packet.fadeDist(), packet.maxDist(), packet.volume(), false));
                    soundManager.play(new RestrictedMovingFadingDistanceSwitchingSound<>(entity, packet.farSound().value(), packet.category(), packet.volume(), packet.pitch(), predicate, packet.stopOnDeath(), packet.fadeDist(), packet.maxDist(), packet.volume(), true));
                }
            }
        });
        registry.playToClient(CooldownChangePacket.PACKET_TYPE, CooldownChangePacket.CODEC, (packet, ctx) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            Item item = packet.item();
            int additional = packet.additional();
            ((CooldownInterface)player.getCooldowns()).frozenLib$changeCooldown(item, additional);
        });
        registry.playToClient(ForcedCooldownPacket.PACKET_TYPE, ForcedCooldownPacket.CODEC, (packet, ctx) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            Item item = packet.item();
            int startTime = packet.startTime();
            int endTime = packet.endTime();
            player.getCooldowns().cooldowns.put(item, new ItemCooldowns.CooldownInstance(startTime, endTime));
        });
        registry.playToClient(CooldownTickCountPacket.PACKET_TYPE, CooldownTickCountPacket.CODEC, (packet, ctx) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCooldowns().tickCount = packet.count();
            }
        });
        registry.playToClient(ScreenShakePacket.PACKET_TYPE, ScreenShakePacket.CODEC, (packet, ctx) -> {
            float intensity = packet.intensity();
            int duration = packet.duration();
            int fallOffStart = packet.falloffStart();
            Vec3 pos = packet.pos();
            float maxDistance = packet.maxDistance();
            int ticks = packet.ticks();

            ClientLevel level = Minecraft.getInstance().level;
            ScreenShaker.addShake(level, intensity, duration, fallOffStart, pos, maxDistance, ticks);
        });

        registry.playToClient(EntityScreenShakePacket.PACKET_TYPE, EntityScreenShakePacket.CODEC, (packet, ctx) -> {
            int id = packet.entityId();
            float intensity = packet.intensity();
            int duration = packet.duration();
            int fallOffStart = packet.falloffStart();
            float maxDistance = packet.maxDistance();
            int ticks = packet.ticks();

            ClientLevel level = Minecraft.getInstance().level;
            Entity entity = level.getEntity(id);
            if (entity != null) {
                ScreenShaker.addShake(entity, intensity, duration, fallOffStart, maxDistance, ticks);
            }
        });
        registry.playToClient(RemoveScreenShakePacket.PACKET_TYPE, RemoveScreenShakePacket.CODEC, (packet, ctx) -> {
            ScreenShaker.SCREEN_SHAKES.removeIf(
                    clientScreenShake -> !(clientScreenShake instanceof ScreenShaker.ClientEntityScreenShake)
            );
        });
        registry.playToClient(RemoveEntityScreenShakePacket.PACKET_TYPE, RemoveEntityScreenShakePacket.CODEC, (packet, ctx) -> {
            int id = packet.entityId();

            ClientLevel level = Minecraft.getInstance().level;
            Entity entity = level.getEntity(id);
            if (entity != null) {
                ScreenShaker.SCREEN_SHAKES.removeIf(clientScreenShake -> clientScreenShake instanceof ScreenShaker.ClientEntityScreenShake entityScreenShake && entityScreenShake.getEntity() == entity);
            }
        });
        registry.playToClient(SpottingIconPacket.PACKET_TYPE, SpottingIconPacket.CODEC, (packet, ctx) -> {
            int id = packet.entityId();
            ResourceLocation texture = packet.texture();
            float startFade = packet.startFade();
            float endFade = packet.endFade();
            ResourceLocation predicate = packet.restrictionID();

            ClientLevel level = Minecraft.getInstance().level;
            Entity entity = level.getEntity(id);
            if (entity instanceof EntitySpottingIconInterface livingEntity) {
                livingEntity.getSpottingIconManager().setIcon(texture, startFade, endFade, predicate);
            }
        });
        registry.playToClient(SpottingIconRemovePacket.PACKET_TYPE, SpottingIconRemovePacket.CODEC, (packet, ctx) -> {
            int id = packet.entityId();

            ClientLevel level = Minecraft.getInstance().level;
            Entity entity = level.getEntity(id);
            if (entity instanceof EntitySpottingIconInterface livingEntity) {
                livingEntity.getSpottingIconManager().icon = null;
            }
        });
        registry.playToClient(WindSyncPacket.PACKET_TYPE, WindSyncPacket.CODEC, (packet, ctx) -> {
            ClientWindManager.time = packet.windTime();
            ClientWindManager.setSeed(packet.seed());
            ClientWindManager.overrideWind = packet.override();
            ClientWindManager.commandWind = packet.commandWind();
            ClientWindManager.hasInitialized = true;
        });
        registry.playToClient(WindDisturbancePacket.PACKET_TYPE, WindDisturbancePacket.CODEC, (packet, ctx) -> {
            ClientLevel level = Minecraft.getInstance().level;
            long posOrID = packet.posOrID();
            Optional<WindDisturbanceLogic> disturbanceLogic = WindDisturbanceLogic.getWindDisturbanceLogic(packet.id());
            if (disturbanceLogic.isPresent()) {
                WindDisturbanceLogic.SourceType sourceType = packet.disturbanceSourceType();
                Optional source = Optional.empty();
                if (sourceType == WindDisturbanceLogic.SourceType.ENTITY) {
                    source = Optional.ofNullable(level.getEntity((int) posOrID));
                } else if (sourceType == WindDisturbanceLogic.SourceType.BLOCK_ENTITY) {
                    source = Optional.ofNullable(level.getBlockEntity(BlockPos.of(posOrID)));
                }

                ClientWindManager.addWindDisturbance(
                        new WindDisturbance(
                                source,
                                packet.origin(),
                                packet.affectedArea(),
                                disturbanceLogic.get()
                        )
                );
            }
        });
        //C2S

        registry.playToClient(ImprovedGoalDebugPayload.PACKET_TYPE, ImprovedGoalDebugPayload.STREAM_CODEC, (packet, ctx) -> {
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
            if(entity != null) {
                net.frozenblock.lib.debug.client.impl.DebugRenderManager.improvedGoalSelectorRenderer.addGoalSelector(
                        entity,
                        packet.goals()
                );
            }
        });

        registry.playToClient(GoalDebugRemovePayload.PACKET_TYPE, GoalDebugRemovePayload.STREAM_CODEC, (packet, ctx) -> {
            net.frozenblock.lib.debug.client.impl.DebugRenderManager.improvedGoalSelectorRenderer.removeGoalSelector(packet.entityId());
        });

        registry.playToClient(ImprovedGameEventListenerDebugPayload.PACKET_TYPE, ImprovedGameEventListenerDebugPayload.STREAM_CODEC, (packet, ctx) -> {
            net.frozenblock.lib.debug.client.impl.DebugRenderManager.improvedGameEventRenderer.trackListener(
                    packet.listenerPos(),
                    packet.listenerRange()
            );
        });

        registry.playToClient(ImprovedGameEventDebugPayload.PACKET_TYPE, ImprovedGameEventDebugPayload.STREAM_CODEC, (packet, ctx) -> {
            net.frozenblock.lib.debug.client.impl.DebugRenderManager.improvedGameEventRenderer.trackGameEvent(
                    packet.gameEventType(),
                    packet.pos()
            );
        });

        registry.playToClient(WindAccessPacket.PACKET_TYPE, WindAccessPacket.STREAM_CODEC, (packet, ctx) -> {
           ClientWindManager.addAccessedPosition(packet.accessPos());
        });
    }

    public static boolean isLocalPlayer(Player player) {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
            return false;

        return Minecraft.getInstance().isLocalPlayer(player.getGameProfile().getId());
    }

    public static boolean connectedToIntegratedServer() {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
            return false;
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.hasSingleplayerServer();
    }
    /**
     * @return if the client is connected to any server
     */
    public static boolean connectedToServer() {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
            return false;
        Minecraft minecraft = Minecraft.getInstance();
        ClientPacketListener listener = minecraft.getConnection();
        if (listener == null)
            return false;
        return listener.getConnection().isConnected();
    }

    /**
     * @return if the current server is multiplayer (LAN/dedicated) or not (singleplayer)
     */
    public static boolean isMultiplayer() {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
            return true;

        return !Minecraft.getInstance().isSingleplayer();
    }
}
