package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.event.api.ClientEvent;
import net.frozenblock.lib.event.api.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void startClientTick(CallbackInfo info) {
        NeoForge.EVENT_BUS.post(new ClientTickEvent.Start((Minecraft) (Object)this));
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void endClientTick(CallbackInfo info) {
        NeoForge.EVENT_BUS.post(new ClientTickEvent.End((Minecraft) (Object)this));
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gameThread:Ljava/lang/Thread;", shift = At.Shift.AFTER, ordinal = 0), method = "run")
    private void clientStarted(CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new ClientEvent.Started((Minecraft) (Object)this));
    }
}
