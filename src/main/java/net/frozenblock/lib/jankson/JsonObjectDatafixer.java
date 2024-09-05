package net.frozenblock.lib.jankson;

import com.mojang.datafixers.DataFixer;
import org.jetbrains.annotations.NotNull;

public interface JsonObjectDatafixer {
    void dataFix(@NotNull DataFixer dataFixer, int newVersion);
}
