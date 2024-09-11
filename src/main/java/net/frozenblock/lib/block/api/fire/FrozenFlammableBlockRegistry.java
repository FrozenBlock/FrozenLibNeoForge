package net.frozenblock.lib.block.api.fire;

import lombok.Getter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class FrozenFlammableBlockRegistry {


    private static final Map<Block, FrozenFlammableBlock> map = new HashMap<>();

    public static FrozenFlammableBlock get(Block block) {
        if(!map.containsKey(block)) map.put(block, new FrozenFlammableBlock());
        return map.get(block);
    }

    public static FrozenFlammableBlock get() {
        return get(Blocks.FIRE);
    }

    public static class FrozenFlammableBlock {
        private FrozenFlammableBlock() {}
        private final HashMap<Block, Entry> map = new HashMap<>();
        public void add(Block block, int spreadChance, int burnChance) {
            this.map.put(block, new Entry(spreadChance, burnChance));
        }

        public Entry get(Block block) {
            return map.get(block);
        }

        @Getter
        public static class Entry {
            private final int spreadChance,burnChance;
            private Entry(int spreadChance, int burnChance) {
                this.spreadChance = spreadChance;
                this.burnChance = burnChance;
            }

        }
    }
}
