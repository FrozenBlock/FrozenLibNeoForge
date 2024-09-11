package net.frozenblock.lib.block.mixin.fire;

import net.frozenblock.lib.block.api.fire.FireBlockInfo;
import net.frozenblock.lib.block.api.fire.FrozenFlammableBlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin implements FireBlockInfo {

    @Shadow
    public abstract int getBurnOdds(BlockState state);

    @Shadow
    public abstract int getIgniteOdds(BlockState state);

    @Unique
    private FrozenFlammableBlockRegistry.FrozenFlammableBlock frozenLibNeoForge$block;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(BlockBehaviour.Properties properties, CallbackInfo ci) {
        frozenLibNeoForge$block = FrozenFlammableBlockRegistry.get((Block)(Object)this);
    }

    @SuppressWarnings("all")
    @Inject(at = @At("HEAD"), method = "getBurnOdds", cancellable = true)
    private void addBurn(BlockState state, CallbackInfoReturnable cir) {
        final Integer get = frozenLibNeoForge$get(state, true);
        if(get != null)
            cir.setReturnValue(get);
    }

    @SuppressWarnings("all")
    @Inject(at = @At("HEAD"), method = "getIgniteOdds", cancellable = true)
    private void addIgnite(BlockState state, CallbackInfoReturnable cir) {
        final Integer get = frozenLibNeoForge$get(state, false);
        if(get != null)
            cir.setReturnValue(get);
    }

    @Unique
    private Integer frozenLibNeoForge$get(BlockState state, boolean burn) {
        final FrozenFlammableBlockRegistry.FrozenFlammableBlock.Entry i = frozenLibNeoForge$block.get(state.getBlock());
        if(i != null) {
            if(state.hasProperty(BlockStateProperties.WATERLOGGED) && state.hasProperty(BlockStateProperties.WATERLOGGED))
                return 0;
            else
                return burn ? i.getBurnChance() : i.getSpreadChance();
        }
        return null;
    }

    @Override
    public int getSpreadChance(BlockState block) {
        return getIgniteOdds(block);
    }

    @Override
    public int getBurnChance(BlockState block) {
        return getBurnOdds(block);
    }
}
