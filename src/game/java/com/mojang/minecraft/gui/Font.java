package com.mojang.minecraft.gui;

import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.opengl.ImageData;
import org.lwjgl.opengl.GL11;

public final class Font {
	private int[] charWidths = new int[256];
	private int fontTexture = 0;

	public Font(String var1, Textures var2) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ImageData var3 = ImageData.loadImageFile(EagRuntime.getResourceStream(var1));
		int var4 = var3.getWidth();
		int var5 = var3.getHeight();
		int[] var6 = new int[var4 * var5];
		var3.getRGB(0, 0, var4, var5, var6, 0, var4);

		for(int var14 = 0; var14 < 128; ++var14) {
			var5 = var14 % 16;
			int var7 = var14 / 16;
			int var8 = 0;

			for(boolean var9 = false; var8 < 8 && !var9; ++var8) {
				int var10 = (var5 << 3) + var8;
				var9 = true;

				for(int var11 = 0; var11 < 8 && var9; ++var11) {
					int var12 = ((var7 << 3) + var11) * var4;
					var12 = var6[var10 + var12] & 255;
					if(var12 > 128) {
						var9 = false;
					}
				}
			}

			if(var14 == 32) {
				var8 = 4;
			}

			this.charWidths[var14] = var8;
		}

		this.fontTexture = var2.getTextureId(var1);
	}

	public final void drawShadow(String var1, int var2, int var3, int var4) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.draw(var1, var2 + 1, var3 + 1, var4, true);
		this.draw(var1, var2, var3, var4);
	}

	public final void draw(String var1, int var2, int var3, int var4) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.draw(var1, var2, var3, var4, false);
	}

	private void draw(String var1, int var2, int var3, int var4, boolean var5) {
		if(var1 != null) {
			char[] var12 = var1.toCharArray();
			if(var5) {
				var4 = (var4 & 16579836) >> 2;
			}

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTexture);
			Tesselator var6 = Tesselator.instance;
			var6.begin();
			var6.color(var4);
			int var7 = 0;

			for(int var8 = 0; var8 < var12.length; ++var8) {
				int var9;
				if(var12[var8] == 38 && var12.length > var8 + 1) {
					var4 = "0123456789abcdef".indexOf(var12[var8 + 1]);
					if(var4 < 0) {
						var4 = 15;
					}

					var9 = (var4 & 8) << 3;
					int var10 = (var4 & 1) * 191 + var9;
					int var11 = ((var4 & 2) >> 1) * 191 + var9;
					var4 = ((var4 & 4) >> 2) * 191 + var9;
					var4 = var4 << 16 | var11 << 8 | var10;
					var8 += 2;
					if(var5) {
						var4 = (var4 & 16579836) >> 2;
					}

					var6.color(var4);
				}

				var4 = var12[var8] % 16 << 3;
				var9 = var12[var8] / 16 << 3;
				float var13 = 7.99F;
				var6.vertexUV((float)(var2 + var7), (float)var3 + var13, 0.0F, (float)var4 / 128.0F, ((float)var9 + var13) / 128.0F);
				var6.vertexUV((float)(var2 + var7) + var13, (float)var3 + var13, 0.0F, ((float)var4 + var13) / 128.0F, ((float)var9 + var13) / 128.0F);
				var6.vertexUV((float)(var2 + var7) + var13, (float)var3, 0.0F, ((float)var4 + var13) / 128.0F, (float)var9 / 128.0F);
				var6.vertexUV((float)(var2 + var7), (float)var3, 0.0F, (float)var4 / 128.0F, (float)var9 / 128.0F);
				var7 += this.charWidths[var12[var8]];
			}

			var6.end();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}

	public final int width(String var1) {
		if(var1 == null) {
			return 0;
		} else {
			char[] var4 = var1.toCharArray();
			int var2 = 0;

			for(int var3 = 0; var3 < var4.length; ++var3) {
				if(var4[var3] == 38) {
					++var3;
				} else {
					var2 += this.charWidths[var4[var3]];
				}
			}

			return var2;
		}
	}
}
