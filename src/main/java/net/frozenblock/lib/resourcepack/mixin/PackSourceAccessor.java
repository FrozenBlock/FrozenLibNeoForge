package net.frozenblock.lib.resourcepack.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.UnaryOperator;

@Mixin(PackSource.class)
public interface PackSourceAccessor {
    @Invoker("decorateWithSource")
    static UnaryOperator<Component> invokeDecorateWithSource(String translationKey) {
        throw new AssertionError("Mixin injection failed");
    }
}
