package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class FallingTile extends Tile {
	public FallingTile(int var1, int var2) {
		super(var1, var2);
	}

	public final void onBlockAdded(Level var1, int var2, int var3, int var4) {
		tryToFall(var1, var2, var3, var4);
	}

	public final void neighborChanged(Level var1, int var2, int var3, int var4, int var5) {
		tryToFall(var1, var2, var3, var4);
	}

	private static void tryToFall(Level var0, int var1, int var2, int var3) {
		int var4 = var1;
		int var5 = var2;

		int var6;
		for(var6 = var3; var0.getTile(var4, var5 - 1, var6) == 0 && var5 > 0; --var5) {
		}

		if(var5 != var2) {
			var0.swap(var1, var2, var3, var4, var5, var6);
		}

	}
}
