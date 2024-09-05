package net.frozenblock.lib.event.api;

import net.frozenblock.lib.gravity.api.GravityContext;
import net.neoforged.bus.api.Event;

public class GravityModificationEvent extends Event {
    public final GravityContext context;

    public GravityModificationEvent(final GravityContext context) {
        this.context = context;
    }
}
