package net.frozenblock.lib.event.api;

import net.frozenblock.lib.mobcategory.impl.FrozenMobCategory;
import net.frozenblock.lib.worldgen.vein.impl.FrozenVeinType;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;

public class FrozenVeinEvent extends Event {
    private final ArrayList<FrozenVeinType> context;

    public FrozenVeinEvent (final ArrayList<FrozenVeinType> context) {
        this.context = context;
    }

    public void registerCategory(final FrozenVeinType category) {
        context.add(category);
    }
}
