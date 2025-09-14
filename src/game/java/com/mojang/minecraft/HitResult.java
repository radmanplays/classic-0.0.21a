package com.mojang.minecraft;

import com.mojang.minecraft.player.Player;

public final class HitResult {
	public int x;
	public int y;
	public int z;
	public int f;

	public HitResult(int var1, int var2, int var3, int var4, int var5) {
		this.x = var2;
		this.y = var3;
		this.z = var4;
		this.f = var5;
	}

	float distanceTo(Player var1, int var2) {
		int var3 = this.x;
		int var4 = this.y;
		int var5 = this.z;
		if(var2 == 1) {
			if(this.f == 0) {
				--var4;
			}

			if(this.f == 1) {
				++var4;
			}

			if(this.f == 2) {
				--var5;
			}

			if(this.f == 3) {
				++var5;
			}

			if(this.f == 4) {
				--var3;
			}

			if(this.f == 5) {
				++var3;
			}
		}

		float var6 = (float)var3 - var1.x;
		float var8 = (float)var4 - var1.y;
		float var7 = (float)var5 - var1.z;
		return var6 * var6 + var8 * var8 + var7 * var7;
	}
}
