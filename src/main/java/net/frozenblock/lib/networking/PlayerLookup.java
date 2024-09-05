package net.frozenblock.lib.networking;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;

public class PlayerLookup {
    public static Collection<ServerPlayer> all(MinecraftServer server) {
        return Collections.unmodifiableCollection(server.getPlayerList().getPlayers());
    }
}
