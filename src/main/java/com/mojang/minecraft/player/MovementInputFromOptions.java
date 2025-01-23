package com.mojang.minecraft.player;

public final class MovementInputFromOptions extends MovementInput {
	private boolean[] keys = new boolean[10];

	public final void setKey(int var1, boolean var2) {
		byte var3 = -1;
		if(var1 == 200 || var1 == 17) {
			var3 = 0;
		}

		if(var1 == 208 || var1 == 31) {
			var3 = 1;
		}

		if(var1 == 203 || var1 == 30) {
			var3 = 2;
		}

		if(var1 == 205 || var1 == 32) {
			var3 = 3;
		}

		if(var1 == 57 || var1 == 219) {
			var3 = 4;
		}

		if(var3 >= 0) {
			this.keys[var3] = var2;
		}

	}

	public final void releaseAllKeys() {
		for(int var1 = 0; var1 < 10; ++var1) {
			this.keys[var1] = false;
		}

	}

	public final void updatePlayerMoveState() {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;
		if(this.keys[0]) {
			--this.moveForward;
		}

		if(this.keys[1]) {
			++this.moveForward;
		}

		if(this.keys[2]) {
			--this.moveStrafe;
		}

		if(this.keys[3]) {
			++this.moveStrafe;
		}

		this.jumpHeld = this.keys[4];
	}
}
