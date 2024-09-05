package net.frozenblock.lib.event.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class ClientEvent<T> extends Event implements IModBusEvent {
    public final T source;

    private ClientEvent(T source) {
        this.source = source;
    }

    public static class Started extends ClientEvent<Minecraft> {
        public Started(Minecraft minecraft) {
            super(minecraft);
        }
    }

    /**
     * Use GameShuttingDownEvent instead
     * */
    @Deprecated
    public static class Stopping extends ClientEvent<Minecraft> {
        public Stopping(Minecraft minecraft) {
            super(minecraft);
        }
    }

    public static class LevelEnd extends ClientEvent<ClientLevel> {
        public LevelEnd(ClientLevel source) {
            super(source);
        }
    }

    public static class LevelStart extends ClientEvent<ClientLevel> {
        public LevelStart(ClientLevel source) {
            super(source);
        }
    }
}
