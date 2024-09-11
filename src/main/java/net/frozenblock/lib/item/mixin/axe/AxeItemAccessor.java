package net.frozenblock.lib.item.mixin.axe;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {
    @Accessor("STRIPPABLES")
    static Map<Block, Block> accessStrip() {
        throw new AssertionError();
    }

    @Accessor("STRIPPABLES")
    @Mutable
    static void setStrip(Map<Block, Block> map) {
        throw new AssertionError();
    }


}
