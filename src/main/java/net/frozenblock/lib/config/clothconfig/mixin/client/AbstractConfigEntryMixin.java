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

package net.frozenblock.lib.config.clothconfig.mixin.client;

import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import net.frozenblock.lib.config.clothconfig.impl.DisableableWidgetInterface;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(AbstractConfigEntry.class)
@Debug(export = true)
public class AbstractConfigEntryMixin {

	@Inject(method = "save", at = @At("HEAD"), cancellable = true, remap = false)
	public void frozenLib$save(CallbackInfo info) {
		if (!((DisableableWidgetInterface) this).frozenLib$getEntryPermissionType().canModify) {
			info.cancel();
		}
	}
}
