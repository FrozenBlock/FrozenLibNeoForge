package net.frozenblock.lib;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforgespi.language.IModInfo;

public class FrozenBools {

    public static boolean isInitialized;

    public static boolean useNewDripstoneLiquid = false;

    // EXTERNAL MODS
    public static final boolean HAS_ARCHITECTURY = hasMod("architectury");
    public static final boolean HAS_C2ME = hasMod("c2me");
    public static final boolean HAS_CLOTH_CONFIG = hasMod("cloth-config");
    public static final boolean HAS_INDIUM = hasMod("indium");
    public static final boolean HAS_IRIS = hasMod("iris");
    public static final boolean HAS_MOONLIGHT_LIB = hasMod("moonlight");
    public static final boolean HAS_SIMPLE_COPPER_PIPES = hasMod("copper_pipe");
    public static final boolean HAS_SODIUM = hasMod("sodium");
    public static final boolean HAS_STARLIGHT = hasMod("starlight");
    public static final boolean HAS_TERRABLENDER = hasMod("terrablender");
    public static final boolean HAS_TERRALITH = hasMod("terralith");

    public static boolean hasMod(String id) {
        if(ModList.get() == null) {
            for(IModInfo info : LoadingModList.get().getMods()) {
                if (info.getModId().equals(id)) return true;
            }
        } else {
            for (IModInfo info : ModList.get().getMods()) {
                if (info.getModId().equals(id)) return true;
            }
        }
        return false;
    }
}
