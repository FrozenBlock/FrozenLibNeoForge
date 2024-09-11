package net.frozenblock.lib.resourcepack.api;

import net.frozenblock.lib.resourcepack.mixin.PackSourceAccessor;
import net.minecraft.server.packs.repository.PackSource;

public class FrozenPackSource {
    /**
     * A built in resourcepack, but not enabled by default.*/
    public static final PackSource BUILT_IN_NODEFAULT = PackSource.create(PackSourceAccessor.invokeDecorateWithSource("pack.source.builtin"), false);
}
