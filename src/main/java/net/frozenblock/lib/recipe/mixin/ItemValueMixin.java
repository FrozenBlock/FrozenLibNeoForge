/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.recipe.mixin;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(Ingredient.ItemValue.class)
public class ItemValueMixin {

	@Redirect(method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/serialization/MapCodec;codec()Lcom/mojang/serialization/Codec;",
			ordinal = 0
		)
	)
	private static Codec<Ingredient.ItemValue> frozenLib$newCodec(
			MapCodec<Ingredient.ItemValue> map
	) {
		return RecordCodecBuilder.create(instance ->
			instance.group(
				ItemStack.SIMPLE_ITEM_CODEC.fieldOf("item").forGetter(Ingredient.ItemValue::item),
				DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
					.forGetter(stack -> stack.item().getComponentsPatch())
			).apply(instance, (item, patch) -> {
                item.applyComponents(patch);
                return new Ingredient.ItemValue(item);
			})
		);
	}
}
