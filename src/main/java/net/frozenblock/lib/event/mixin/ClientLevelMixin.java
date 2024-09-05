package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.event.api.ClientEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "tickEntities", at = @At("TAIL"))
    public void tickWorldAfterBlockEntities(CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new ClientEvent.LevelEnd((ClientLevel) (Object)this));
    }

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new ClientEvent.LevelStart((ClientLevel) (Object)this));
    }
}
