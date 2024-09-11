package net.frozenblock.lib.blockEntity.api;

import net.minecraft.world.level.block.Block;

public interface FrozenBlockEntityType {
    /**
     * Allows registration of valid blocks to an already existing block entity*/
    void addValidBlocks(Block ... blocks);
}
