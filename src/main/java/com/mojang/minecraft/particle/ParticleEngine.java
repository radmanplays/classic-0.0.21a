package com.mojang.minecraft.particle;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.adapter.RealOpenGLEnums;

public final class ParticleEngine {
	public List particles = new ArrayList();
	private Textures textures;

	public ParticleEngine(Level var1, Textures var2) {
		this.textures = var2;
	}

	public final void render(Player var1, float var2) {
		if(this.particles.size() != 0) {
			GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			int var3 = this.textures.getTextureId("/terrain.png");
			GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, var3);
			float var12 = -((float)Math.cos((double)var1.yRot * Math.PI / 180.0D));
			float var4 = -((float)Math.sin((double)var1.yRot * Math.PI / 180.0D));
			float var5 = -var4 * (float)Math.sin((double)var1.xRot * Math.PI / 180.0D);
			float var6 = var12 * (float)Math.sin((double)var1.xRot * Math.PI / 180.0D);
			float var11 = (float)Math.cos((double)var1.xRot * Math.PI / 180.0D);
			Tesselator var7 = Tesselator.instance;
			var7.begin();

			for(int var8 = 0; var8 < this.particles.size(); ++var8) {
				Particle var9 = (Particle)this.particles.get(var8);
				float var10 = 0.8F * var9.getBrightness();
				var7.color(var10, var10, var10);
				var9.render(var7, var2, var12, var11, var4, var5, var6);
			}

			var7.end();
			GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		}
	}
}
