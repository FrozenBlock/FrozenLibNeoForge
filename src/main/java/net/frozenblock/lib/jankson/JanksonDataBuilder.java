package net.frozenblock.lib.jankson;

import blue.endless.jankson.Jankson;
import com.mojang.datafixers.DataFixer;
import org.jetbrains.annotations.Nullable;

public class JanksonDataBuilder extends Jankson.Builder {

    @Nullable
    private DataFixer fixer;
    @Nullable
    private Integer version;


    public static Jankson.Builder withFixer(final Jankson.Builder builder, @Nullable final DataFixer fixer) {
        if(builder instanceof JanksonDataBuilder dataBuilder) {
            dataBuilder.fixer = fixer;
        }
        return builder;
    }

    public static Jankson.Builder withVersion(final Jankson.Builder builder, @Nullable final Integer version) {
        if(builder instanceof JanksonDataBuilder dataBuilder) {
            dataBuilder.version = version;
        }
        return builder;
    }

    public static Jankson.Builder withBoth(final Jankson.Builder builder, @Nullable final DataFixer fixer, @Nullable final Integer version) {
        return withVersion(withFixer(builder, fixer), version);
    }

    public static @Nullable DataFixer getFixer(final Jankson.Builder builder) {
        return builder instanceof JanksonDataBuilder dataBuilder ? dataBuilder.fixer : null;
    }

    public static @Nullable Integer getVersion(final Jankson.Builder builder) {
        return builder instanceof JanksonDataBuilder dataBuilder ? dataBuilder.version : null;
    }
}
