package net.frozenblock.lib.loot.api;

import lombok.experimental.UtilityClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.Resource;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@UtilityClass
public class FrozenLootTableSource {
    public static final ThreadLocal<Map<ResourceLocation, Boolean>> SOURCES = ThreadLocal.withInitial(HashMap::new);
    private static final WeakHashMap<PackResources, PackSource> PACK_SOURCES = new WeakHashMap<>();

    public static boolean determineSource(@Nullable Resource value) {
        if(value != null) {
            PackSource source = getSource(value.source());
            return source == PackSource.BUILT_IN;
        }
        return false;
    }

    private static PackSource getSource(PackResources source) {
        return PACK_SOURCES.getOrDefault(source, PackSource.DEFAULT);
    }

    public static void setSource(PackResources pack, PackSource source) {
        PACK_SOURCES.put(pack, source);
    }
}
