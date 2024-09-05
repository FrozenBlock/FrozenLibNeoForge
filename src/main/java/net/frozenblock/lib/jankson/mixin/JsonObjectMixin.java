package net.frozenblock.lib.jankson.mixin;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.frozenblock.lib.config.api.instance.json.JanksonOps;
import net.frozenblock.lib.jankson.JanksonEntry;
import net.frozenblock.lib.jankson.JsonObjectDatafixer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import xjs.data.Json;

import java.util.ArrayList;
import java.util.List;

@Mixin(JsonObject.class)
public class JsonObjectMixin implements JsonObjectDatafixer {

    private static final DSL.TypeReference TYPE = () -> "JANKSON";

    @Override
    public void dataFix(@NotNull DataFixer dataFixer, int newVersion) {
        boolean shouldAddVersion = true;
        final JsonObject that = ((JsonObject)(Object)this);
        String versionKey = "jankson:schema_version";
        for(String key : that.keySet()) {
            if(versionKey.equals(key)) {
                shouldAddVersion = false;
                break;
            }
        }


        if (shouldAddVersion) {
            final List<JanksonEntry> converter = new ArrayList<>();
            for(String key : that.keySet()) {
                final JsonElement value = that.get(key);
                @Nullable final String comment = that.getComment(key);
                converter.add(new JanksonEntry(key, comment, value));
            }
            that.clear();
            that.put(versionKey, JanksonOps.INSTANCE.createNumeric(newVersion), "The version of this JSON file\nDon't modify!");
            for(JanksonEntry entry : converter) {
                that.put(entry.key(), entry.element(), entry.comment());
            }
        }

        JsonElement versionEntry = null;
        for(String key : that.keySet()) {
            if(key.equals(versionKey)) {
                versionEntry = that.get(key);
                break;
            }
        }
        assert versionEntry != null;
        int version = ((JsonPrimitive)versionEntry).asInt(newVersion);

        for(String key : that.keySet()) {
            Dynamic<JsonElement> dynamic = new Dynamic<>(JanksonOps.INSTANCE, that.get(key));
            @Nullable final String comment = that.getComment(key);
            that.put(key, dataFixer.update(TYPE, dynamic, version, newVersion).getValue(), comment);
        }
    }
}
