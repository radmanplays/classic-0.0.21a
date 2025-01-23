package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import java.util.Random;

public final class CalmLiquidTile extends LiquidTile {
	protected CalmLiquidTile(int var1, Liquid var2) {
		super(var1, var2);
		this.tileId = var1 - 1;
		this.calmTileId = var1;
		this.setTicking(false);
	}

	public final void tick(Level var1, int var2, int var3, int var4, Random var5) {
	}

	public final void neighborChanged(Level var1, int var2, int var3, int var4, int var5) {
		boolean var6 = false;
		if(var1.getTile(var2 - 1, var3, var4) == 0) {
			var6 = true;
		}

		if(var1.getTile(var2 + 1, var3, var4) == 0) {
			var6 = true;
		}

		if(var1.getTile(var2, var3, var4 - 1) == 0) {
			var6 = true;
		}

		if(var1.getTile(var2, var3, var4 + 1) == 0) {
			var6 = true;
		}

		if(var1.getTile(var2, var3 - 1, var4) == 0) {
			var6 = true;
		}

		if(var5 != 0) {
			Liquid var7 = Tile.tiles[var5].getLiquidType();
			if(this.liquid == Liquid.water && var7 == Liquid.lava || var7 == Liquid.water && this.liquid == Liquid.lava) {
				var1.setTile(var2, var3, var4, Tile.rock.id);
				return;
			}
		}

		if(var6) {
			var1.setTileNoUpdate(var2, var3, var4, this.tileId);
			var1.addToTickNextTick(var2, var3, var4, this.tileId);
		}

	}
}
