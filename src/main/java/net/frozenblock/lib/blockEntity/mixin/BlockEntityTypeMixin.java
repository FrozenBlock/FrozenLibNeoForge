package net.frozenblock.lib.blockEntity.mixin;

import net.frozenblock.lib.blockEntity.api.FrozenBlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements FrozenBlockEntityType {

    @Shadow @Final private Set<Block> validBlocks;

    @Override
    public void addValidBlocks(Block... blocks) {
        validBlocks.addAll(Arrays.asList(blocks));
    }
}
