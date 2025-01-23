package com.mojang.minecraft.renderer.texture;

import com.mojang.minecraft.level.tile.Tile;

public final class TextureWaterFX extends TextureFX {
	private float[] red = new float[256];
	private float[] green = new float[256];
	private float[] blue = new float[256];
	private float[] alpha = new float[256];
	private int tickCounter = 0;

	public TextureWaterFX() {
		super(Tile.water.tex);
	}

	public final void onTick() {
		++this.tickCounter;

		int var1;
		int var2;
		float var3;
		for(var1 = 0; var1 < 16; ++var1) {
			for(var2 = 0; var2 < 16; ++var2) {
				var3 = 0.0F;

				for(int var4 = var1 - 1; var4 <= var1 + 1; ++var4) {
					int var5 = var4 & 15;
					int var6 = var2 & 15;
					var3 += this.red[var5 + (var6 << 4)];
				}

				this.green[var1 + (var2 << 4)] = var3 / 3.3F + this.blue[var1 + (var2 << 4)] * 0.8F;
			}
		}

		for(var1 = 0; var1 < 16; ++var1) {
			for(var2 = 0; var2 < 16; ++var2) {
				this.blue[var1 + (var2 << 4)] += this.alpha[var1 + (var2 << 4)] * 0.05F;
				if(this.blue[var1 + (var2 << 4)] < 0.0F) {
					this.blue[var1 + (var2 << 4)] = 0.0F;
				}

				this.alpha[var1 + (var2 << 4)] -= 0.1F;
				if(Math.random() < 0.05D) {
					this.alpha[var1 + (var2 << 4)] = 0.5F;
				}
			}
		}

		float[] var7 = this.green;
		this.green = this.red;
		this.red = var7;

		for(var2 = 0; var2 < 256; ++var2) {
			var3 = this.red[var2];
			if(var3 > 1.0F) {
				var3 = 1.0F;
			}

			if(var3 < 0.0F) {
				var3 = 0.0F;
			}

			float var8 = var3 * var3;
			this.imageData[var2 << 2] = (byte)((int)(32.0F + var8 * 32.0F));
			this.imageData[(var2 << 2) + 1] = (byte)((int)(50.0F + var8 * 64.0F));
			this.imageData[(var2 << 2) + 2] = -1;
			this.imageData[(var2 << 2) + 3] = (byte)((int)(146.0F + var8 * 50.0F));
		}

	}
}
