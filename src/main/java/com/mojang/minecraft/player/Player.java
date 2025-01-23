package com.mojang.minecraft.player;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;

public class Player extends Entity {
	private MovementInput input;
	public Inventory inventory = new Inventory();
	public byte userType = 0;

	public Player(Level var1, MovementInput var2) {
		super(var1);
		this.heightOffset = 1.62F;
		this.input = var2;
	}

	public void tick() {
		super.tick();
		boolean var1 = this.isInWater();
		boolean var2 = this.isInLava();
		this.input.updatePlayerMoveState();
		if(this.input.jumpHeld) {
			if(var1) {
				this.yd += 0.04F;
			} else if(var2) {
				this.yd += 0.04F;
			} else if(this.onGround && !this.input.jump) {
				this.yd = 0.42F;
				this.input.jump = true;
			}
		} else {
			this.input.jump = false;
		}

		float var3;
		if(var1) {
			var3 = this.y;
			this.moveRelative(this.input.moveStrafe, this.input.moveForward, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.8F;
			this.yd *= 0.8F;
			this.zd *= 0.8F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + var3, this.zd)) {
				this.yd = 0.3F;
			}

		} else if(var2) {
			var3 = this.y;
			this.moveRelative(this.input.moveStrafe, this.input.moveForward, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.5F;
			this.yd *= 0.5F;
			this.zd *= 0.5F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + var3, this.zd)) {
				this.yd = 0.3F;
			}

		} else {
			this.moveRelative(this.input.moveStrafe, this.input.moveForward, this.onGround ? 0.1F : 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.91F;
			this.yd *= 0.98F;
			this.zd *= 0.91F;
			this.yd = (float)((double)this.yd - 0.08D);
			if(this.onGround) {
				var3 = 0.6F;
				this.xd *= var3;
				this.zd *= var3;
			}

		}
	}

	public void releaseAllKeys() {
		this.input.releaseAllKeys();
	}

	public void setKey(int var1, boolean var2) {
		this.input.setKey(var1, var2);
	}
}
