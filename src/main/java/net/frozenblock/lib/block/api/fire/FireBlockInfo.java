package net.frozenblock.lib.block.api.fire;

import net.minecraft.world.level.block.state.BlockState;

public interface FireBlockInfo {
    int getSpreadChance(BlockState block);
    int getBurnChance(BlockState block);
}
