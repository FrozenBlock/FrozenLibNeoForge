package net.frozenblock.lib.jankson;

import blue.endless.jankson.JsonElement;
import org.jetbrains.annotations.Nullable;

public record JanksonEntry(String key, @Nullable String comment, JsonElement element) {
}
