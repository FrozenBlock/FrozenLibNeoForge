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

package net.frozenblock.lib.particle.api;

import com.mojang.serialization.MapCodec;
import lombok.experimental.UtilityClass;
import net.frozenblock.lib.FrozenSharedConstants;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@UtilityClass
public class FrozenParticleTypes {
	public static final SimpleParticleType DEBUG_POS = new SimpleParticleType(false);

	public static void registerParticles(RegisterEvent.RegisterHelper<ParticleType<?>> registry) {
		FrozenSharedConstants.LOGGER.info("Registering Particles for " + FrozenSharedConstants.MOD_ID);
		registry.register(FrozenSharedConstants.id("debug_pos"), DEBUG_POS);
	}

	@NotNull
	private static <T extends ParticleOptions> ParticleType<T> generate(
		boolean alwaysShow,
		Function<ParticleType<T>, MapCodec<T>> codecGetter,
		Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter
	) {
		return new ParticleType<T>(alwaysShow) {
			@Override
			public MapCodec<T> codec() {
				return codecGetter.apply(this);
			}

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return streamCodecGetter.apply(this);
			}
		};
	}
}
