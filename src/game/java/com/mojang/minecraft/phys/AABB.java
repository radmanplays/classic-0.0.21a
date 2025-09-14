package com.mojang.minecraft.phys;

import java.io.Serializable;

public class AABB implements Serializable {
	public static final long serialVersionUID = 0L;
	private float epsilon = 0.0F;
	public float x0;
	public float y0;
	public float z0;
	public float x1;
	public float y1;
	public float z1;

	public AABB(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.x0 = var1;
		this.y0 = var2;
		this.z0 = var3;
		this.x1 = var4;
		this.y1 = var5;
		this.z1 = var6;
	}

	public AABB expand(float var1, float var2, float var3) {
		float var4 = this.x0;
		float var5 = this.y0;
		float var6 = this.z0;
		float var7 = this.x1;
		float var8 = this.y1;
		float var9 = this.z1;
		if(var1 < 0.0F) {
			var4 += var1;
		}

		if(var1 > 0.0F) {
			var7 += var1;
		}

		if(var2 < 0.0F) {
			var5 += var2;
		}

		if(var2 > 0.0F) {
			var8 += var2;
		}

		if(var3 < 0.0F) {
			var6 += var3;
		}

		if(var3 > 0.0F) {
			var9 += var3;
		}

		return new AABB(var4, var5, var6, var7, var8, var9);
	}

	public AABB grow(float var1, float var2, float var3) {
		float var4 = this.x0 - var1;
		float var5 = this.y0 - var2;
		float var6 = this.z0 - var3;
		var1 += this.x1;
		var2 += this.y1;
		float var7 = this.z1 + var3;
		return new AABB(var4, var5, var6, var1, var2, var7);
	}

	public AABB cloneMove(float var1, float var2, float var3) {
		return new AABB(this.x0 + var3, this.y0 + var2, this.z0 + var3, this.x1 + var1, this.y1 + var2, this.z1 + var3);
	}

	public float clipXCollide(AABB var1, float var2) {
		if(var1.y1 > this.y0 && var1.y0 < this.y1) {
			if(var1.z1 > this.z0 && var1.z0 < this.z1) {
				float var3;
				if(var2 > 0.0F && var1.x1 <= this.x0) {
					var3 = this.x0 - var1.x1 - this.epsilon;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.x0 >= this.x1) {
					var3 = this.x1 - var1.x0 + this.epsilon;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public float clipYCollide(AABB var1, float var2) {
		if(var1.x1 > this.x0 && var1.x0 < this.x1) {
			if(var1.z1 > this.z0 && var1.z0 < this.z1) {
				float var3;
				if(var2 > 0.0F && var1.y1 <= this.y0) {
					var3 = this.y0 - var1.y1 - this.epsilon;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.y0 >= this.y1) {
					var3 = this.y1 - var1.y0 + this.epsilon;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public float clipZCollide(AABB var1, float var2) {
		if(var1.x1 > this.x0 && var1.x0 < this.x1) {
			if(var1.y1 > this.y0 && var1.y0 < this.y1) {
				float var3;
				if(var2 > 0.0F && var1.z1 <= this.z0) {
					var3 = this.z0 - var1.z1 - this.epsilon;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.z0 >= this.z1) {
					var3 = this.z1 - var1.z0 + this.epsilon;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public boolean intersects(AABB var1) {
		return var1.x1 > this.x0 && var1.x0 < this.x1 ? (var1.y1 > this.y0 && var1.y0 < this.y1 ? var1.z1 > this.z0 && var1.z0 < this.z1 : false) : false;
	}

	public void move(float var1, float var2, float var3) {
		this.x0 += var1;
		this.y0 += var2;
		this.z0 += var3;
		this.x1 += var1;
		this.y1 += var2;
		this.z1 += var3;
	}
}
