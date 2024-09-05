package net.frozenblock.lib.event.api;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.Event;

public class ClientTickEvent extends Event {
    public final Minecraft minecraft;

    private ClientTickEvent(final Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public static class Start extends ClientTickEvent {

        public Start(Minecraft minecraft) {
            super(minecraft);
        }
    }

    public static class End extends ClientTickEvent {

        public End(Minecraft minecraft) {
            super(minecraft);
        }
    }

}
