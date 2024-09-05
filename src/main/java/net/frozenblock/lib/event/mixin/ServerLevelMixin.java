package net.frozenblock.lib.event.mixin;

import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.event.api.PlayerJoinEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "addPlayer", at = @At("TAIL"))
    private void frozenLib$addPlayer(ServerPlayer player, CallbackInfo info) {
        NeoForge.EVENT_BUS.post(new PlayerJoinEvent.Level(this.server, ServerLevel.class.cast(this), player));
    }
}
