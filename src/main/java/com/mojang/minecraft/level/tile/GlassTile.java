package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class GlassTile extends Tile {
	private boolean renderAdjacentFaces = false;

	protected GlassTile(int var1, int var2, boolean var3) {
		super(20, 49);
	}

	public final boolean isSolid() {
		return false;
	}

	protected final boolean shouldRenderFace(Level var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = var1.getTile(var2, var3, var4);
		return !this.renderAdjacentFaces && var7 == this.id ? false : super.shouldRenderFace(var1, var2, var3, var4, var5, var6);
	}

	public final boolean blocksLight() {
		return false;
	}
}
