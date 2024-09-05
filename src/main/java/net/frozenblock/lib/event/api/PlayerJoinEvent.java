package net.frozenblock.lib.event.api;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class PlayerJoinEvent extends Event {

    public final MinecraftServer server;
    public final ServerPlayer player;

    private PlayerJoinEvent(final MinecraftServer server, final ServerPlayer player) {
        this.server = server;
        this.player = player;
    }

    /**
     * The event that is triggered when a player joins the server.
     */
    public static class Server extends PlayerJoinEvent {

        public Server(MinecraftServer server, ServerPlayer player) {
            super(server, player);
        }
    }

    /**
     * The event that is triggered when a player joins a world.
     */
    public static class Level extends PlayerJoinEvent {
        public final ServerLevel level;

        public Level(MinecraftServer server, ServerLevel level, ServerPlayer player) {
            super(server, player);
            this.level = level;
        }
    }
}
