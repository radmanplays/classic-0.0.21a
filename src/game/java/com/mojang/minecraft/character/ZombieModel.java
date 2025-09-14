package com.mojang.minecraft.character;

public final class ZombieModel {
	private Cube head = new Cube(0, 0);
	private Cube body;
	private Cube arm0;
	private Cube arm1;
	private Cube leg0;
	private Cube leg1;

	public ZombieModel() {
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		this.body = new Cube(16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
		this.arm0 = new Cube(40, 16);
		this.arm0.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
		this.arm0.setPos(-5.0F, 2.0F, 0.0F);
		this.arm1 = new Cube(40, 16);
		this.arm1.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
		this.arm1.setPos(5.0F, 2.0F, 0.0F);
		this.leg0 = new Cube(0, 16);
		this.leg0.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
		this.leg0.setPos(-2.0F, 12.0F, 0.0F);
		this.leg1 = new Cube(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
		this.leg1.setPos(2.0F, 12.0F, 0.0F);
	}

	public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.head.yRot = var4 / 57.29578F;
		this.head.xRot = var5 / 57.29578F;
		this.arm0.xRot = (float)Math.cos((double)var1 * 0.6662D + Math.PI) * 2.0F * var2;
		this.arm0.zRot = (float)(Math.cos((double)var1 * 0.2312D) + 1.0D) * var2;
		this.arm1.xRot = (float)Math.cos((double)var1 * 0.6662D) * 2.0F * var2;
		this.arm1.zRot = (float)(Math.cos((double)var1 * 0.2812D) - 1.0D) * var2;
		this.leg0.xRot = (float)Math.cos((double)var1 * 0.6662D) * 1.4F * var2;
		this.leg1.xRot = (float)Math.cos((double)var1 * 0.6662D + Math.PI) * 1.4F * var2;
		this.arm0.zRot += (float)Math.cos((double)var3 * 0.09D) * 0.05F + 0.05F;
		this.arm1.zRot -= (float)Math.cos((double)var3 * 0.09D) * 0.05F + 0.05F;
		this.arm0.xRot += (float)Math.sin((double)var3 * 0.067D) * 0.05F;
		this.arm1.xRot -= (float)Math.sin((double)var3 * 0.067D) * 0.05F;
		this.head.render(var6);
		this.body.render(var6);
		this.arm0.render(var6);
		this.arm1.render(var6);
		this.leg0.render(var6);
		this.leg1.render(var6);
	}
}
