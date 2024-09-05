package net.frozenblock.lib.block.mixin;

import net.frozenblock.lib.block.api.LootTableInjection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(at = @At("HEAD"), method = "getLootTable", cancellable = true)
    public void inject(CallbackInfoReturnable<ResourceKey<LootTable>> cir) {
        if(((BlockBehaviour)(Object)this) instanceof LootTableInjection that) {
            cir.setReturnValue(that.getLootTableForInject());
        }
    }
}
