package com.mojang.minecraft.renderer.texture;

import com.mojang.minecraft.level.tile.Tile;

public final class TextureLavaFX extends TextureFX {
	private float[] red = new float[256];
	private float[] green = new float[256];
	private float[] blue = new float[256];
	private float[] alpha = new float[256];

	public TextureLavaFX() {
		super(Tile.lava.tex);
	}

	public final void onTick() {
		int var2;
		float var3;
		int var5;
		int var6;
		int var7;
		for(int var1 = 0; var1 < 16; ++var1) {
			for(var2 = 0; var2 < 16; ++var2) {
				var3 = 0.0F;
				int var4 = (int)(Math.sin((double)var2 * Math.PI * 2.0D / 16.0D) * (double)1.2F);
				var5 = (int)(Math.sin((double)var1 * Math.PI * 2.0D / 16.0D) * (double)1.2F);

				for(var6 = var1 - 1; var6 <= var1 + 1; ++var6) {
					for(var7 = var2 - 1; var7 <= var2 + 1; ++var7) {
						int var8 = var6 + var4 & 15;
						int var9 = var7 + var5 & 15;
						var3 += this.red[var8 + (var9 << 4)];
					}
				}

				this.green[var1 + (var2 << 4)] = var3 / 10.0F + (this.blue[(var1 & 15) + ((var2 & 15) << 4)] + this.blue[(var1 + 1 & 15) + ((var2 & 15) << 4)] + this.blue[(var1 + 1 & 15) + ((var2 + 1 & 15) << 4)] + this.blue[(var1 & 15) + ((var2 + 1 & 15) << 4)]) / 4.0F * 0.8F;
				this.blue[var1 + (var2 << 4)] += this.alpha[var1 + (var2 << 4)] * 0.01F;
				if(this.blue[var1 + (var2 << 4)] < 0.0F) {
					this.blue[var1 + (var2 << 4)] = 0.0F;
				}

				this.alpha[var1 + (var2 << 4)] -= 0.06F;
				if(Math.random() < 0.005D) {
					this.alpha[var1 + (var2 << 4)] = 1.5F;
				}
			}
		}

		float[] var10 = this.green;
		this.green = this.red;
		this.red = var10;

		for(var2 = 0; var2 < 256; ++var2) {
			var3 = this.red[var2];
			if(var3 > 1.0F) {
				var3 = 1.0F;
			}

			if(var3 < 0.0F) {
				var3 = 0.0F;
			}

			var5 = (int)(var3 * 200.0F + 55.0F);
			var6 = (int)(var3 * var3 * 255.0F);
			var7 = (int)(var3 * var3 * var3 * var3 * 128.0F);
			this.imageData[var2 << 2] = (byte)var5;
			this.imageData[(var2 << 2) + 1] = (byte)var6;
			this.imageData[(var2 << 2) + 2] = (byte)var7;
			this.imageData[(var2 << 2) + 3] = -1;
		}

	}
}
