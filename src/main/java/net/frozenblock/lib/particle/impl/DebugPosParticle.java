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

package net.frozenblock.lib.particle.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DebugPosParticle extends TextureSheetParticle {

	DebugPosParticle(@NotNull ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
		this.setSize(0.1F, 0.1F);
		this.quadSize = 0.1F;
		this.lifetime = 1;
		this.hasPhysics = false;
		this.gravity = 0.0F;
	}

	@Override
	public void tick() {
		this.remove();
	}

	@Override
	@NotNull
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(SpriteSet sprites) {
			this.sprite = sprites;
		}

		@Override
		@NotNull
		public Particle createParticle(@NotNull SimpleParticleType defaultParticleType, @NotNull ClientLevel clientLevel, double x, double y, double z, double g, double h, double i) {
			DebugPosParticle debugPosParticle = new DebugPosParticle(clientLevel, x, y, z);
			debugPosParticle.pickSprite(this.sprite);
			return debugPosParticle;
		}
	}
}
