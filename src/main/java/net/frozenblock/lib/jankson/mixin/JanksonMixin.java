package net.frozenblock.lib.jankson.mixin;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import com.mojang.datafixers.DataFixer;
import net.frozenblock.lib.jankson.JanksonDataBuilder;
import net.frozenblock.lib.jankson.JsonObjectDatafixer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;

@Mixin(Jankson.class)
public class JanksonMixin {
    @Shadow
    private JsonObject root;
    @Nullable
    private DataFixer dataFixer;
    @Nullable
    private Integer version;

    @Inject(at = @At("RETURN"), method = "<init>(Lblue/endless/jankson/Jankson$Builder;)V")
    private void init(Jankson.Builder builder, CallbackInfo ci) {
        dataFixer = JanksonDataBuilder.getFixer(builder);
        version = JanksonDataBuilder.getVersion(builder);
    }

    @Inject(at = @At("RETURN"), method = "load(Ljava/io/InputStream;)Lblue/endless/jankson/JsonObject;")
    public void load(InputStream in, CallbackInfoReturnable<JsonObject> cir) {
        @Nullable DataFixer fixer = this.dataFixer;
        @Nullable Integer newVersion = this.version;
        if (fixer != null && newVersion != null) {
            assert root != null;
            ((JsonObjectDatafixer)root).dataFix(fixer, newVersion);
        }
    }
}
