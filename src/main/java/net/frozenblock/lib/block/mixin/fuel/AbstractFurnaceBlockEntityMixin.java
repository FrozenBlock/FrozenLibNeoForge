package net.frozenblock.lib.block.mixin.fuel;

import com.mojang.datafixers.util.Either;
import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.block.api.fuel.FuelEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.ObjIntConsumer;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

    @Shadow
    private static void add(ObjIntConsumer<Either<Item, TagKey<Item>>> consumer, ItemLike item, int time) {
        FrozenSharedConstants.LOGGER.warn("Injection failed {}add({};{};{}I)V", AbstractFurnaceBlockEntityMixin.class, ObjIntConsumer.class, ItemLike.class, ItemLike.class);
    }

    @Shadow
    private static void add(ObjIntConsumer<Either<Item, TagKey<Item>>> consumer, TagKey<Item> tag, int time) {
        FrozenSharedConstants.LOGGER.warn("Injection failed {}add({};{};{}I)V", AbstractFurnaceBlockEntityMixin.class, ObjIntConsumer.class, TagKey.class, ItemLike.class);

    }

    @Inject(at = @At("HEAD"), method = "buildFuels")
    private static void addCustom(ObjIntConsumer<Either<Item, TagKey<Item>>> map1, CallbackInfo ci) {
        final FuelEvent event = new FuelEvent();
        NeoForge.EVENT_BUS.post(event);
        event.forItemLike((itemLike, i) -> add(map1, itemLike, i));
        event.forItemtag((tag, i) -> add(map1, tag, i));
    }

}
