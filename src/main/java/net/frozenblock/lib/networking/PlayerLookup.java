package net.frozenblock.lib.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class PlayerLookup {
    public static Collection<ServerPlayer> all(MinecraftServer server) {
        return Collections.unmodifiableCollection(server.getPlayerList().getPlayers());
    }

    public static Collection<ServerPlayer> tracking(BlockEntity blockEntity) {
        return tracking((ServerLevel) blockEntity.getLevel(), blockEntity.getBlockPos());
    }

    public static Collection<ServerPlayer> tracking(ServerLevel level, ChunkPos pos) {
        return Objects.requireNonNull(level).getChunkSource().chunkMap.getPlayers(pos, false);
    }

    public static Collection<ServerPlayer> tracking(ServerLevel level, BlockPos pos) {
        return tracking(level, new ChunkPos(pos));
    }
}
