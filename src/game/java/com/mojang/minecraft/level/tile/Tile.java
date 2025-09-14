package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;
import java.util.Random;

public class Tile {
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	private static int[] tickSpeed = new int[256];
	public static final Tile rock;
	public static final Tile grass;
	public static final Tile dirt;
	public static final Tile wood;
	public static final Tile stoneBrick;
	public static final Tile bush;
	public static final Tile unbreakable;
	public static final Tile water;
	public static final Tile calmWater;
	public static final Tile lava;
	public static final Tile calmLava;
	public static final Tile sand;
	public static final Tile gravel;
	public static final Tile oreGold;
	public static final Tile oreIron;
	public static final Tile oreCoal;
	public static final Tile log;
	public static final Tile leaf;
	public static final Tile sponge;
	public static final Tile glass;
	public static final Tile clothRed;
	public static final Tile clothOrange;
	public static final Tile clothYellow;
	public static final Tile clothChartreuse;
	public static final Tile clothGreen;
	public static final Tile clothSpringGreen;
	public static final Tile clothCyan;
	public static final Tile clothCapri;
	public static final Tile clothUltramarine;
	public static final Tile clothViolet;
	public static final Tile clothPurple;
	public static final Tile clothMagenta;
	public static final Tile clothRose;
	public static final Tile clothDarkGray;
	public static final Tile clothGray;
	public static final Tile clothWhite;
	public static final Tile plantYellow;
	public static final Tile plantRed;
	public static final Tile mushroomBrown;
	public static final Tile mushroomRed;
	public static final Tile blockGold;
	public int tex;
	public final int id;
	private float xx0;
	private float yy0;
	private float zz0;
	private float xx1;
	private float yy1;
	private float zz1;
	public float particleGravity;

	protected Tile(int var1) {
		new Random();
		tiles[var1] = this;
		this.id = var1;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	protected final void setTicking(boolean var1) {
		shouldTick[this.id] = var1;
	}

	protected final void setShape(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.xx0 = var1;
		this.yy0 = var2;
		this.zz0 = var3;
		this.xx1 = var4;
		this.yy1 = var5;
		this.zz1 = var6;
	}

	protected Tile(int var1, int var2) {
		this(var1);
		this.tex = var2;
	}

	public final void setTickSpeed(int var1) {
		tickSpeed[this.id] = 16;
	}

	public boolean render(Tesselator var1, Level var2, int var3, int var4, int var5, int var6) {
		boolean var7 = false;
		float var8 = 0.5F;
		float var9 = 0.8F;
		float var10 = 0.6F;
		float var11;
		if(this.shouldRenderFace(var2, var4, var5 - 1, var6, var3, 0)) {
			var11 = this.getBrightness(var2, var4, var5 - 1, var6);
			var1.color(var8 * var11, var8 * var11, var8 * var11);
			this.renderFace(var1, var4, var5, var6, 0);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4, var5 + 1, var6, var3, 1)) {
			var11 = this.getBrightness(var2, var4, var5 + 1, var6);
			var1.color(var11 * 1.0F, var11 * 1.0F, var11 * 1.0F);
			this.renderFace(var1, var4, var5, var6, 1);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4, var5, var6 - 1, var3, 2)) {
			var11 = this.getBrightness(var2, var4, var5, var6 - 1);
			var1.color(var9 * var11, var9 * var11, var9 * var11);
			this.renderFace(var1, var4, var5, var6, 2);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4, var5, var6 + 1, var3, 3)) {
			var11 = this.getBrightness(var2, var4, var5, var6 + 1);
			var1.color(var9 * var11, var9 * var11, var9 * var11);
			this.renderFace(var1, var4, var5, var6, 3);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4 - 1, var5, var6, var3, 4)) {
			var11 = this.getBrightness(var2, var4 - 1, var5, var6);
			var1.color(var10 * var11, var10 * var11, var10 * var11);
			this.renderFace(var1, var4, var5, var6, 4);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4 + 1, var5, var6, var3, 5)) {
			var11 = this.getBrightness(var2, var4 + 1, var5, var6);
			var1.color(var10 * var11, var10 * var11, var10 * var11);
			this.renderFace(var1, var4, var5, var6, 5);
			var7 = true;
		}

		return var7;
	}

	protected float getBrightness(Level var1, int var2, int var3, int var4) {
		return var1.getBrightness(var2, var3, var4);
	}

	protected boolean shouldRenderFace(Level var1, int var2, int var3, int var4, int var5, int var6) {
		return var5 == 1 ? false : !var1.isSolidTile(var2, var3, var4);
	}

	protected int getTexture(int var1) {
		return this.tex;
	}

	public void renderFace(Tesselator var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getTexture(var5);
		int var7 = var6 % 16 << 4;
		var6 = var6 / 16 << 4;
		float var8 = (float)var7 / 256.0F;
		float var17 = ((float)var7 + 15.99F) / 256.0F;
		float var9 = (float)var6 / 256.0F;
		float var16 = ((float)var6 + 15.99F) / 256.0F;
		float var10 = (float)var2 + this.xx0;
		float var14 = (float)var2 + this.xx1;
		float var11 = (float)var3 + this.yy0;
		float var15 = (float)var3 + this.yy1;
		float var12 = (float)var4 + this.zz0;
		float var13 = (float)var4 + this.zz1;
		if(var5 == 0) {
			var1.vertexUV(var10, var11, var13, var8, var16);
			var1.vertexUV(var10, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var12, var17, var9);
			var1.vertexUV(var14, var11, var13, var17, var16);
		} else if(var5 == 1) {
			var1.vertexUV(var14, var15, var13, var17, var16);
			var1.vertexUV(var14, var15, var12, var17, var9);
			var1.vertexUV(var10, var15, var12, var8, var9);
			var1.vertexUV(var10, var15, var13, var8, var16);
		} else if(var5 == 2) {
			var1.vertexUV(var10, var15, var12, var17, var9);
			var1.vertexUV(var14, var15, var12, var8, var9);
			var1.vertexUV(var14, var11, var12, var8, var16);
			var1.vertexUV(var10, var11, var12, var17, var16);
		} else if(var5 == 3) {
			var1.vertexUV(var10, var15, var13, var8, var9);
			var1.vertexUV(var10, var11, var13, var8, var16);
			var1.vertexUV(var14, var11, var13, var17, var16);
			var1.vertexUV(var14, var15, var13, var17, var9);
		} else if(var5 == 4) {
			var1.vertexUV(var10, var15, var13, var17, var9);
			var1.vertexUV(var10, var15, var12, var8, var9);
			var1.vertexUV(var10, var11, var12, var8, var16);
			var1.vertexUV(var10, var11, var13, var17, var16);
		} else if(var5 == 5) {
			var1.vertexUV(var14, var11, var13, var8, var16);
			var1.vertexUV(var14, var11, var12, var17, var16);
			var1.vertexUV(var14, var15, var12, var17, var9);
			var1.vertexUV(var14, var15, var13, var8, var9);
		}
	}

	public final void renderBackFace(Tesselator var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getTexture(var5);
		float var7 = (float)(var6 % 16) / 16.0F;
		float var8 = var7 + 0.999F / 16.0F;
		float var16 = (float)(var6 / 16) / 16.0F;
		float var9 = var16 + 0.999F / 16.0F;
		float var10 = (float)var2 + this.xx0;
		float var14 = (float)var2 + this.xx1;
		float var11 = (float)var3 + this.yy0;
		float var15 = (float)var3 + this.yy1;
		float var12 = (float)var4 + this.zz0;
		float var13 = (float)var4 + this.zz1;
		if(var5 == 0) {
			var1.vertexUV(var14, var11, var13, var8, var9);
			var1.vertexUV(var14, var11, var12, var8, var16);
			var1.vertexUV(var10, var11, var12, var7, var16);
			var1.vertexUV(var10, var11, var13, var7, var9);
		}

		if(var5 == 1) {
			var1.vertexUV(var10, var15, var13, var7, var9);
			var1.vertexUV(var10, var15, var12, var7, var16);
			var1.vertexUV(var14, var15, var12, var8, var16);
			var1.vertexUV(var14, var15, var13, var8, var9);
		}

		if(var5 == 2) {
			var1.vertexUV(var10, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var12, var7, var9);
			var1.vertexUV(var14, var15, var12, var7, var16);
			var1.vertexUV(var10, var15, var12, var8, var16);
		}

		if(var5 == 3) {
			var1.vertexUV(var14, var15, var13, var8, var16);
			var1.vertexUV(var14, var11, var13, var8, var9);
			var1.vertexUV(var10, var11, var13, var7, var9);
			var1.vertexUV(var10, var15, var13, var7, var16);
		}

		if(var5 == 4) {
			var1.vertexUV(var10, var11, var13, var8, var9);
			var1.vertexUV(var10, var11, var12, var7, var9);
			var1.vertexUV(var10, var15, var12, var7, var16);
			var1.vertexUV(var10, var15, var13, var8, var16);
		}

		if(var5 == 5) {
			var1.vertexUV(var14, var15, var13, var7, var16);
			var1.vertexUV(var14, var15, var12, var8, var16);
			var1.vertexUV(var14, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var13, var7, var9);
		}

	}

	public static void renderFaceNoTexture(Entity var0, Tesselator var1, int var2, int var3, int var4, int var5) {
		float var6 = (float)var2;
		float var7 = (float)var2 + 1.0F;
		float var8 = (float)var3;
		float var9 = (float)var3 + 1.0F;
		float var10 = (float)var4;
		float var11 = (float)var4 + 1.0F;
		if(var5 == 0 && (float)var3 > var0.y) {
			var1.vertex(var6, var8, var11);
			var1.vertex(var6, var8, var10);
			var1.vertex(var7, var8, var10);
			var1.vertex(var7, var8, var11);
		}

		if(var5 == 1 && (float)var3 < var0.y) {
			var1.vertex(var7, var9, var11);
			var1.vertex(var7, var9, var10);
			var1.vertex(var6, var9, var10);
			var1.vertex(var6, var9, var11);
		}

		if(var5 == 2 && (float)var4 > var0.z) {
			var1.vertex(var6, var9, var10);
			var1.vertex(var7, var9, var10);
			var1.vertex(var7, var8, var10);
			var1.vertex(var6, var8, var10);
		}

		if(var5 == 3 && (float)var4 < var0.z) {
			var1.vertex(var6, var9, var11);
			var1.vertex(var6, var8, var11);
			var1.vertex(var7, var8, var11);
			var1.vertex(var7, var9, var11);
		}

		if(var5 == 4 && (float)var2 > var0.x) {
			var1.vertex(var6, var9, var11);
			var1.vertex(var6, var9, var10);
			var1.vertex(var6, var8, var10);
			var1.vertex(var6, var8, var11);
		}

		if(var5 == 5 && (float)var2 < var0.x) {
			var1.vertex(var7, var8, var11);
			var1.vertex(var7, var8, var10);
			var1.vertex(var7, var9, var10);
			var1.vertex(var7, var9, var11);
		}

	}

	public AABB getTileAABB(int var1, int var2, int var3) {
		return new AABB((float)var1, (float)var2, (float)var3, (float)(var1 + 1), (float)(var2 + 1), (float)(var3 + 1));
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
	}

	public final void destroy(Level var1, int var2, int var3, int var4, ParticleEngine var5) {
		for(int var6 = 0; var6 < 4; ++var6) {
			for(int var7 = 0; var7 < 4; ++var7) {
				for(int var8 = 0; var8 < 4; ++var8) {
					float var9 = (float)var2 + ((float)var6 + 0.5F) / (float)4;
					float var10 = (float)var3 + ((float)var7 + 0.5F) / (float)4;
					float var11 = (float)var4 + ((float)var8 + 0.5F) / (float)4;
					Particle var12 = new Particle(var1, var9, var10, var11, var9 - (float)var2 - 0.5F, var10 - (float)var3 - 0.5F, var11 - (float)var4 - 0.5F, this);
					var5.particles.add(var12);
				}
			}
		}

	}

	public Liquid getLiquidType() {
		return Liquid.none;
	}

	public void neighborChanged(Level var1, int var2, int var3, int var4, int var5) {
	}

	public void onBlockAdded(Level var1, int var2, int var3, int var4) {
	}

	public int getTickDelay() {
		return 0;
	}

	public void onTileAdded(Level var1, int var2, int var3, int var4) {
	}

	public void onTileRemoved(Level var1, int var2, int var3, int var4) {
	}

	static {
		Tile var10000 = new Tile(1, 1);
		float var1 = 1.0F;
		float var0 = 1.0F;
		Tile var2 = var10000;
		var2.particleGravity = var1;
		rock = var2;
		GrassTile var13 = new GrassTile(2);
		var1 = 1.0F;
		var0 = 0.9F;
		GrassTile var3 = var13;
		var3.particleGravity = var1;
		grass = var3;
		DirtTile var14 = new DirtTile(3, 2);
		var1 = 1.0F;
		var0 = 0.8F;
		DirtTile var4 = var14;
		var4.particleGravity = var1;
		dirt = var4;
		var10000 = new Tile(4, 16);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		wood = var2;
		var10000 = new Tile(5, 4);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		stoneBrick = var2;
		Bush var15 = new Bush(6, 15);
		var1 = 1.0F;
		var0 = 0.7F;
		Bush var5 = var15;
		var5.particleGravity = var1;
		bush = var5;
		var10000 = new Tile(7, 17);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		unbreakable = var2;
		LiquidTile var16 = new LiquidTile(8, Liquid.water);
		var1 = 1.0F;
		var0 = 1.0F;
		LiquidTile var6 = var16;
		var6.particleGravity = var1;
		water = var6;
		CalmLiquidTile var17 = new CalmLiquidTile(9, Liquid.water);
		var1 = 1.0F;
		var0 = 1.0F;
		CalmLiquidTile var7 = var17;
		var7.particleGravity = var1;
		calmWater = var7;
		var16 = new LiquidTile(10, Liquid.lava);
		var1 = 1.0F;
		var0 = 1.0F;
		var6 = var16;
		var6.particleGravity = var1;
		lava = var6;
		var17 = new CalmLiquidTile(11, Liquid.lava);
		var1 = 1.0F;
		var0 = 1.0F;
		var7 = var17;
		var7.particleGravity = var1;
		calmLava = var7;
		FallingTile var18 = new FallingTile(12, 18);
		var1 = 1.0F;
		var0 = 0.8F;
		FallingTile var8 = var18;
		var8.particleGravity = var1;
		sand = var8;
		var18 = new FallingTile(13, 19);
		var1 = 1.0F;
		var0 = 0.8F;
		var8 = var18;
		var8.particleGravity = var1;
		gravel = var8;
		var10000 = new Tile(14, 32);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		oreGold = var2;
		var10000 = new Tile(15, 33);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		oreIron = var2;
		var10000 = new Tile(16, 34);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		oreCoal = var2;
		LogTile var19 = new LogTile(17);
		var1 = 1.0F;
		var0 = 1.0F;
		LogTile var9 = var19;
		var9.particleGravity = var1;
		log = var9;
		LeafTile var20 = new LeafTile(18, 22, true);
		var1 = 0.4F;
		var0 = 1.0F;
		LeafTile var10 = var20;
		var10.particleGravity = var1;
		leaf = var10;
		SpongeTile var21 = new SpongeTile(19);
		var1 = 0.9F;
		var0 = 1.0F;
		SpongeTile var11 = var21;
		var11.particleGravity = var1;
		sponge = var11;
		GlassTile var22 = new GlassTile(20, 49, false);
		var1 = 1.0F;
		var0 = 1.0F;
		GlassTile var12 = var22;
		var12.particleGravity = var1;
		glass = var12;
		var10000 = new Tile(21, 64);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothRed = var2;
		var10000 = new Tile(22, 65);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothOrange = var2;
		var10000 = new Tile(23, 66);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothYellow = var2;
		var10000 = new Tile(24, 67);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothChartreuse = var2;
		var10000 = new Tile(25, 68);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothGreen = var2;
		var10000 = new Tile(26, 69);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothSpringGreen = var2;
		var10000 = new Tile(27, 70);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothCyan = var2;
		var10000 = new Tile(28, 71);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothCapri = var2;
		var10000 = new Tile(29, 72);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothUltramarine = var2;
		var10000 = new Tile(30, 73);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothViolet = var2;
		var10000 = new Tile(31, 74);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothPurple = var2;
		var10000 = new Tile(32, 75);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothMagenta = var2;
		var10000 = new Tile(33, 76);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothRose = var2;
		var10000 = new Tile(34, 77);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothDarkGray = var2;
		var10000 = new Tile(35, 78);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothGray = var2;
		var10000 = new Tile(36, 79);
		var1 = 1.0F;
		var0 = 1.0F;
		var2 = var10000;
		var2.particleGravity = var1;
		clothWhite = var2;
		var15 = new Bush(37, 13);
		var1 = 1.0F;
		var0 = 0.7F;
		var5 = var15;
		var5.particleGravity = var1;
		plantYellow = var5;
		var15 = new Bush(38, 12);
		var1 = 1.0F;
		var0 = 0.7F;
		var5 = var15;
		var5.particleGravity = var1;
		plantRed = var5;
		var15 = new Bush(39, 29);
		var1 = 1.0F;
		var0 = 0.7F;
		var5 = var15;
		var5.particleGravity = var1;
		mushroomBrown = var5;
		var15 = new Bush(40, 28);
		var1 = 1.0F;
		var0 = 0.7F;
		var5 = var15;
		var5.particleGravity = var1;
		mushroomRed = var5;
		var10000 = new Tile(41, 40);
		var1 = 1.0F;
		var0 = 0.7F;
		var2 = var10000;
		var2.particleGravity = var1;
		blockGold = var2;
	}
}
