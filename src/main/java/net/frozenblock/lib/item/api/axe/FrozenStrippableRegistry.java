package net.frozenblock.lib.item.api.axe;

import com.google.common.collect.ImmutableMap;
import net.frozenblock.lib.item.mixin.axe.AxeItemAccessor;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FrozenStrippableRegistry {
    public static void register(Block original, Block stripped) {
        AxeItemAccessor.setStrip(new ImmutableMap.Builder<Block,Block>().putAll(AxeItemAccessor.accessStrip()).put(original, stripped).build());
    }
    public static void register(Consumer<Map<Block, Block>> consumer) {
        final Map<Block, Block> toAdd = new HashMap<>();
        consumer.accept(toAdd);
        AxeItemAccessor.setStrip(new ImmutableMap.Builder<Block, Block>().putAll(AxeItemAccessor.accessStrip()).putAll(toAdd).build());

    }
}
