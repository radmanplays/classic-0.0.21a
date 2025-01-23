package com.mojang.minecraft.net;

public final class PlayerMove {
	public float x;
	public float y;
	public float z;
	public float yRot;
	public float xRot;
	public boolean rotating = false;
	public boolean moving = false;

	public PlayerMove(float var1, float var2, float var3, float var4, float var5) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
		this.yRot = var4;
		this.xRot = var5;
		this.rotating = true;
		this.moving = true;
	}

	public PlayerMove(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
		this.moving = true;
		this.rotating = false;
	}

	public PlayerMove(float var1, float var2) {
		this.yRot = var1;
		this.xRot = var2;
		this.rotating = true;
		this.moving = false;
	}
}
