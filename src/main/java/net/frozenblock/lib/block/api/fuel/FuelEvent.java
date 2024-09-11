package net.frozenblock.lib.block.api.fuel;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FuelEvent extends Event {
    private final Map<ItemLike, Integer> itemLike = new HashMap<>();
    private final Map<TagKey<Item>, Integer> itemTag = new HashMap<>();
    public void add(ItemLike item, int time) {
        itemLike.put(item,time);
    }

    public void add(TagKey<Item> tag, int time) {
        itemTag.put(tag, time);
    }

    public void forItemLike(BiConsumer<ItemLike, Integer> consumer) {
        itemLike.forEach(consumer);
    }

    public void forItemtag(BiConsumer<TagKey<Item>, Integer> consumer) {
        itemTag.forEach(consumer);
    }
}
