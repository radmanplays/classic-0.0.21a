package com.mojang.minecraft.character;

public final class Vec3 {
	public float x;
	public float y;
	public float z;

	public Vec3(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
	}

	public final Vec3 subtract(Vec3 var1) {
		return new Vec3(this.x - var1.x, this.y - var1.y, this.z - var1.z);
	}

	public final Vec3 normalize() {
		float var1 = (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
		return new Vec3(this.x / var1, this.y / var1, this.z / var1);
	}
}
