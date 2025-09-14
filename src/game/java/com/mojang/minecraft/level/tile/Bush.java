package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;
import java.util.Random;

public final class Bush extends Tile {
	protected Bush(int var1, int var2) {
		super(var1);
		this.tex = var2;
		this.setTicking(true);
	}

	public final void tick(Level var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getTile(var2, var3 - 1, var4);
		if(!var1.isLit(var2, var3, var4) || var6 != Tile.dirt.id && var6 != Tile.grass.id) {
			var1.setTile(var2, var3, var4, 0);
		}

	}

	public final boolean render(Tesselator var1, Level var2, int var3, int var4, int var5, int var6) {
		if(var2.isLit(var4, var5, var6) ^ var3 != 1) {
			return false;
		} else {
			int var15 = this.getTexture(15);
			float var17 = (float)(var15 % 16) / 16.0F;
			float var18 = var17 + 0.999F / 16.0F;
			float var16 = (float)(var15 / 16) / 16.0F;
			float var7 = var16 + 0.999F / 16.0F;
			var1.color(255, 255, 255);

			for(int var8 = 0; var8 < 2; ++var8) {
				float var9 = (float)(Math.sin((double)var8 * Math.PI / (double)2 + Math.PI * 0.25D) * 0.5D);
				float var10 = (float)(Math.cos((double)var8 * Math.PI / (double)2 + Math.PI * 0.25D) * 0.5D);
				float var11 = (float)var4 + 0.5F - var9;
				var9 += (float)var4 + 0.5F;
				float var12 = (float)var5;
				float var13 = (float)var5 + 1.0F;
				float var14 = (float)var6 + 0.5F - var10;
				var10 += (float)var6 + 0.5F;
				var1.vertexUV(var11, var13, var14, var18, var16);
				var1.vertexUV(var9, var13, var10, var17, var16);
				var1.vertexUV(var9, var12, var10, var17, var7);
				var1.vertexUV(var11, var12, var14, var18, var7);
				var1.vertexUV(var9, var13, var10, var18, var16);
				var1.vertexUV(var11, var13, var14, var17, var16);
				var1.vertexUV(var11, var12, var14, var17, var7);
				var1.vertexUV(var9, var12, var10, var18, var7);
			}

			return true;
		}
	}

	public final AABB getAABB(int var1, int var2, int var3) {
		return null;
	}

	public final boolean blocksLight() {
		return false;
	}

	public final boolean isSolid() {
		return false;
	}
}
