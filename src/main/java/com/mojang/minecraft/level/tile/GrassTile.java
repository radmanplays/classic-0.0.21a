package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import java.util.Random;

public final class GrassTile extends Tile {
	protected GrassTile(int var1) {
		super(2);
		this.tex = 3;
		this.setTicking(true);
	}

	protected final int getTexture(int var1) {
		return var1 == 1 ? 0 : (var1 == 0 ? 2 : 3);
	}

	public final void tick(Level var1, int var2, int var3, int var4, Random var5) {
		if(var5.nextInt(4) == 0) {
			if(!var1.isLit(var2, var3 + 1, var4)) {
				var1.setTile(var2, var3, var4, Tile.dirt.id);
			} else {
				for(int var9 = 0; var9 < 4; ++var9) {
					int var6 = var2 + var5.nextInt(3) - 1;
					int var7 = var3 + var5.nextInt(5) - 3;
					int var8 = var4 + var5.nextInt(3) - 1;
					if(var1.getTile(var6, var7, var8) == Tile.dirt.id && var1.isLit(var6, var7 + 1, var8)) {
						var1.setTile(var6, var7, var8, Tile.grass.id);
					}
				}

			}
		}
	}
}
