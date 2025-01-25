package com.mojang.minecraft.renderer;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public final class Chunk {
	private Level level;
	private int lists = -1;
	private static Tesselator t = Tesselator.instance;
	public static int updates = 0;
	private int x0;
	private int y0;
	private int z0;
	private int x1;
	private int y1;
	private int z1;
	private boolean[] skipRenderPass = new boolean[2];
	public boolean isInFrustum = false;

	public Chunk(Level var1, int var2, int var3, int var4, int var5, int var6) {
		this.level = var1;
		this.x0 = var2;
		this.y0 = var3;
		this.z0 = var4;
		this.x1 = this.y1 = this.z1 = 16;
		Math.sqrt((double)(this.x1 * this.x1 + this.y1 * this.y1 + this.z1 * this.z1));
		this.lists = var6;
		this.reset();
	}

	public final float a(Player var1) {
		float var2 = var1.x - (float)this.x0;
		float var3 = var1.y - (float)this.y0;
		float var4 = var1.z - (float)this.z0;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	private void reset() {
		for(int var1 = 0; var1 < 2; ++var1) {
			GL11.glNewList(this.lists + var1, GL11.GL_COMPILE);
			GL11.glEndList();
		}

	}

	public final void clear() {
		this.reset();
		this.level = null;
	}

	public final void rebuild() {
		++updates;

		for(int var1 = 0; var1 < 2; ++var1) {
			int var3 = var1;
			Chunk var2 = this;
			int var4 = this.x0;
			int var5 = this.y0;
			int var6 = this.z0;
			int var7 = this.x0 + this.x1;
			int var8 = this.y0 + this.y1;
			int var9 = this.z0 + this.z1;
			GL11.glNewList(this.lists + var1, GL11.GL_COMPILE);
			t.begin();
			boolean var10 = false;

			for(var4 = var4; var4 < var7; ++var4) {
				for(int var11 = var5; var11 < var8; ++var11) {
					for(int var12 = var6; var12 < var9; ++var12) {
						int var13 = var2.level.getTile(var4, var11, var12);
						if(var13 > 0) {
							var10 |= Tile.tiles[var13].render(t, var2.level, var3, var4, var11, var12);
						}
					}
				}
			}

			t.end();
			GL11.glEndList();
			if(var2.skipRenderPass[var3] != !var10) {
				var2.skipRenderPass[var3] = !var10;
			}
		}

	}

	public final void render(IntBuffer var1, int var2) {
		if(this.isInFrustum && !this.skipRenderPass[var2]) {
			var1.put(this.lists + var2);
		}
	}

	public final void isInFrustum(Frustum var1) {
		this.isInFrustum = var1.cubeInFrustum((float)this.x0, (float)this.y0, (float)this.z0, (float)(this.x0 + this.x1), (float)(this.y0 + this.y1), (float)(this.z0 + this.z1));
	}
}
