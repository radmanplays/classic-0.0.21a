package com.mojang.minecraft.level.tile;

public final class LogTile extends Tile {
	protected LogTile(int var1) {
		super(17);
		this.tex = 20;
	}

	protected final int getTexture(int var1) {
		return var1 == 1 ? 21 : (var1 == 0 ? 21 : 20);
	}
}
