package com.mojang.minecraft.particle;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;

public class Particle extends Entity {
	private float xd;
	private float yd;
	private float zd;
	public int tex;
	private float uo;
	private float vo;
	private int age = 0;
	private int lifetime = 0;
	private float size;
	private float gravity;

	public Particle(Level var1, float var2, float var3, float var4, float var5, float var6, float var7, Tile var8) {
		super(var1);
		this.tex = var8.tex;
		this.gravity = var8.particleGravity;
		this.setSize(0.2F, 0.2F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(var2, var3, var4);
		this.xd = var5 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.yd = var6 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.zd = var7 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		float var9 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		var2 = (float)Math.sqrt((double)(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd));
		this.xd = this.xd / var2 * var9 * 0.4F;
		this.yd = this.yd / var2 * var9 * 0.4F + 0.1F;
		this.zd = this.zd / var2 * var9 * 0.4F;
		this.uo = (float)Math.random() * 3.0F;
		this.vo = (float)Math.random() * 3.0F;
		this.size = (float)(Math.random() * 0.5D + 0.5D);
		this.lifetime = (int)(4.0D / (Math.random() * 0.9D + 0.1D));
		this.age = 0;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(this.age++ >= this.lifetime) {
			this.remove();
		}

		this.yd = (float)((double)this.yd - 0.04D * (double)this.gravity);
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.98F;
		this.yd *= 0.98F;
		this.zd *= 0.98F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
		}

	}

	public void render(Tesselator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)(this.tex % 16) + this.uo / 4.0F) / 16.0F;
		float var9 = var8 + 0.999F / 64.0F;
		float var10 = ((float)(this.tex / 16) + this.vo / 4.0F) / 16.0F;
		float var11 = var10 + 0.999F / 64.0F;
		float var12 = 0.1F * this.size;
		float var13 = this.xo + (this.x - this.xo) * var2;
		float var14 = this.yo + (this.y - this.yo) * var2;
		float var15 = this.zo + (this.z - this.zo) * var2;
		var1.vertexUV(var13 - var3 * var12 - var6 * var12, var14 - var4 * var12, var15 - var5 * var12 - var7 * var12, var8, var11);
		var1.vertexUV(var13 - var3 * var12 + var6 * var12, var14 + var4 * var12, var15 - var5 * var12 + var7 * var12, var8, var10);
		var1.vertexUV(var13 + var3 * var12 + var6 * var12, var14 + var4 * var12, var15 + var5 * var12 + var7 * var12, var9, var10);
		var1.vertexUV(var13 + var3 * var12 - var6 * var12, var14 - var4 * var12, var15 + var5 * var12 - var7 * var12, var9, var11);
	}
}
