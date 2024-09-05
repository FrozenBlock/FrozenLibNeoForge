package net.frozenblock.lib.entrypoint.api;

import net.neoforged.fml.loading.FMLEnvironment;

public interface DevelopmentCheck {
    static boolean isDevelopment() {
        return FMLEnvironment.production;
    }
}
