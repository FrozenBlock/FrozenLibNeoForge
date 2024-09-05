package net.frozenblock.lib.event.api;

import net.frozenblock.lib.mobcategory.impl.FrozenMobCategory;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;

public class MobCategoryEvent extends Event {
    private final ArrayList<FrozenMobCategory> context;

    public MobCategoryEvent(final ArrayList<FrozenMobCategory> context) {
        this.context = context;
    }

    public void registerCategory(final FrozenMobCategory category) {
        context.add(category);
    }
}
