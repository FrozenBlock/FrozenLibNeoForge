/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.config.impl.network;

import blue.endless.jankson.api.SyntaxError;
import net.frozenblock.lib.FrozenLogUtils;
import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.ConfigModification;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.network.ConfigByteBufUtil;
import net.frozenblock.lib.config.api.sync.network.ConfigSyncData;
import net.frozenblock.lib.networking.FrozenClientNetworking;
import net.frozenblock.lib.networking.FrozenNetworking;
import net.frozenblock.lib.networking.PlayerLookup;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @since 1.5
 */
public record ConfigSyncPacket<T>(
	String modId,
	String className,
	T configData
) implements CustomPacketPayload {

	public static final int PERMISSION_LEVEL = 2;

	public static final Type<ConfigSyncPacket<?>> PACKET_TYPE = new Type<>(
		FrozenSharedConstants.id("config_sync_packet")
	);
	public static final StreamCodec<FriendlyByteBuf, ConfigSyncPacket<?>> CODEC = StreamCodec.ofMember(ConfigSyncPacket::write, ConfigSyncPacket::create);

	@Nullable
	public static <T> ConfigSyncPacket<T> create(@NotNull FriendlyByteBuf buf) {
		String modId = buf.readUtf();
		String className = buf.readUtf();
		try {
			T configData = ConfigByteBufUtil.readJankson(buf, modId, className);
			return new ConfigSyncPacket<>(modId, className, configData);
		} catch (SyntaxError | ClassNotFoundException e) {
			FrozenLogUtils.logError("Failed to read config data from packet.", true, e);
			return null;
		}
	}

	public void write(@NotNull FriendlyByteBuf buf) {
		buf.writeUtf(modId);
		buf.writeUtf(className);
		ConfigByteBufUtil.writeJankson(buf, modId, configData);
	}

	public static <T> void receive(@NotNull ConfigSyncPacket<T> packet, @Nullable MinecraftServer server) {
		String modId = packet.modId();
		String className = packet.className();
        for (Config<?> raw : ConfigRegistry.getConfigsForMod(modId)) {
			String configClassName = raw.configClass().getName();
            if (!configClassName.equals(className)) continue;
			Config<T> config = (Config<T>) raw;
			if (server != null) {
				// C2S logic
				ConfigModification.copyInto(packet.configData(), config.instance());
				if (!FrozenNetworking.connectedToIntegratedServer())
					config.save();
				for (ServerPlayer player : PlayerLookup.all(server)) {
					sendS2C(player, List.of(config));
				}
			} else {
				// S2C logic
				boolean shouldAddModification = !ConfigRegistry.containsSyncData(config);
				ConfigRegistry.setSyncData(config, new ConfigSyncData<>(packet.configData()));
				if (shouldAddModification) {
					ConfigRegistry.register(
						config,
						new ConfigModification<>(
							new ConfigSyncModification<>(config, ConfigRegistry::getSyncData)
						),
						Integer.MIN_VALUE // make sure it's the first modification
					);
				}
			}
			config.onSync(packet.configData());
			break;
        }
    }

	public static void sendS2C(ServerPlayer player, @NotNull Iterable<Config<?>> configs) {
		if (FrozenNetworking.isLocalPlayer(player))
			return;

		for (Config<?> config : configs) {
			if (!config.supportsSync()) continue;
			ConfigSyncPacket<?> packet = new ConfigSyncPacket<>(config.modId(), config.configClass().getName(), config.config());
			PacketDistributor.sendToPlayer(player, packet);
		}
	}

	public static void sendS2C(ServerPlayer player) {
		sendS2C(player, ConfigRegistry.getAllConfigs());
	}

	public static boolean hasPermissionsToSendSync(@Nullable Player player, boolean serverSide) {
		if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
			return player.hasPermissions(PERMISSION_LEVEL);

		if (FrozenClientNetworking.notConnected())
			return false;

		boolean isHost = serverSide && FrozenNetworking.isLocalPlayer(player);
		return FrozenNetworking.connectedToIntegratedServer() || isHost || player.hasPermissions(PERMISSION_LEVEL);
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendC2S(@NotNull Iterable<Config<?>> configs) {
		//if (!ClientPlayNetworking.canSend(PACKET_TYPE)) return; TODO: CHECK THIS

		for (Config<?> config : configs) {
			if (!config.supportsSync()) continue;
			ConfigSyncPacket<?> packet = new ConfigSyncPacket<>(config.modId(), config.configClass().getName(), config.instance());
			PacketDistributor.sendToServer(packet);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendC2S() {
		sendC2S(ConfigRegistry.getAllConfigs());
	}

	@OnlyIn(Dist.CLIENT)
	public static <T> void trySendC2S(Config<T> config) {
		if(hasPermissionsToSendSync(Minecraft.getInstance().player, false))
			sendC2S(List.of(config));
	}

	@Override
	@NotNull
	public Type<?> type() {
		return PACKET_TYPE;
	}
}
