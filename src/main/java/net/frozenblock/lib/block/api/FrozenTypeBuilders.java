package net.frozenblock.lib.block.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class FrozenTypeBuilders {

    public static BlockSetType copyOf(BlockSetType original, ResourceLocation id) {
        return new BlockSetType(
                id.toString(),
                original.canOpenByHand(),
                original.canOpenByWindCharge(),
                original.canButtonBeActivatedByArrows(),
                original.pressurePlateSensitivity(),
                original.soundType(),
                original.doorClose(),
                original.doorOpen(),
                original.trapdoorClose(),
                original.trapdoorOpen(),
                original.pressurePlateClickOff(),
                original.pressurePlateClickOn(),
                original.buttonClickOff(),
                original.buttonClickOn()
        );
    }

    public static WoodType copyOf(WoodType original, BlockSetType setType, ResourceLocation id) {
        return new WoodType(
                id.toString(),
                setType,
                original.soundType(),
                original.hangingSignSoundType(),
                original.fenceGateClose(),
                original.fenceGateOpen()
        );
    }
}
