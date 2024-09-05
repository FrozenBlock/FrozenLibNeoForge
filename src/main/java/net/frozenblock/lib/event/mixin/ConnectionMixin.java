package net.frozenblock.lib.event.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.frozenblock.lib.FrozenClient;
import net.frozenblock.lib.networking.FrozenClientNetworking;
import net.minecraft.network.Connection;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Connection.class)
public class ConnectionMixin {
    @Inject(at = @At("HEAD"), method = "channelInactive")
    private void disconnectAddon(ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        FrozenClient.disconnect();
        FrozenClientNetworking.registerClientReceivers();
    }

    @Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/DisconnectionDetails;)V"))
    private void disconnectAddon(CallbackInfo ci) {
        FrozenClient.disconnect();
        FrozenClientNetworking.registerClientReceivers();
    }
}
