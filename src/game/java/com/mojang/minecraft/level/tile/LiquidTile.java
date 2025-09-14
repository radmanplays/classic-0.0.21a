package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;
import java.util.Random;

public class LiquidTile extends Tile {
	protected Liquid liquid;
	protected int calmTileId;
	protected int tileId;

	protected LiquidTile(int var1, Liquid var2) {
		super(var1);
		this.liquid = var2;
		this.tex = 14;
		if(var2 == Liquid.lava) {
			this.tex = 30;
		}

		this.tileId = var1;
		this.calmTileId = var1 + 1;
		float var4 = 0.01F;
		float var3 = 0.1F;
		this.setShape(var4 + 0.0F, 0.0F - var3 + var4, var4 + 0.0F, var4 + 1.0F, 1.0F - var3 + var4, var4 + 1.0F);
		this.setTicking(true);
		if(var2 == Liquid.lava) {
			this.setTickSpeed(16);
		}

	}

	public final void onBlockAdded(Level var1, int var2, int var3, int var4) {
		var1.addToTickNextTick(var2, var3, var4, this.tileId);
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
		boolean var7 = false;
		var4 = var4;
		var3 = var3;
		var2 = var2;
		var1 = var1;
		LiquidTile var8 = this;
		boolean var9 = false;

		boolean var6;
		do {
			--var3;
			if(var1.getTile(var2, var3, var4) != 0 || !var8.checkSponge(var1, var2, var3, var4)) {
				break;
			}

			var6 = var1.setTile(var2, var3, var4, var8.tileId);
			if(var6) {
				var9 = true;
			}
		} while(var6 && var8.liquid != Liquid.lava);

		++var3;
		if(var8.liquid == Liquid.water || !var9) {
			var9 |= var8.checkWater(var1, var2 - 1, var3, var4);
			var9 |= var8.checkWater(var1, var2 + 1, var3, var4);
			var9 |= var8.checkWater(var1, var2, var3, var4 - 1);
			var9 |= var8.checkWater(var1, var2, var3, var4 + 1);
		}

		if(!var9) {
			var1.setTileNoUpdate(var2, var3, var4, var8.calmTileId);
		} else {
			var1.addToTickNextTick(var2, var3, var4, var8.tileId);
		}

	}

	private boolean checkSponge(Level var1, int var2, int var3, int var4) {
		if(this.liquid == Liquid.water) {
			for(int var7 = var2 - 2; var7 <= var2 + 2; ++var7) {
				for(int var5 = var3 - 2; var5 <= var3 + 2; ++var5) {
					for(int var6 = var4 - 2; var6 <= var4 + 2; ++var6) {
						if(var1.getTile(var7, var5, var6) == Tile.sponge.id) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean checkWater(Level var1, int var2, int var3, int var4) {
		int var5 = var1.getTile(var2, var3, var4);
		if(var5 == 0) {
			if(!this.checkSponge(var1, var2, var3, var4)) {
				return false;
			}

			boolean var6 = var1.setTile(var2, var3, var4, this.tileId);
			if(var6) {
				var1.addToTickNextTick(var2, var3, var4, this.tileId);
			}
		}

		return false;
	}

	protected final float getBrightness(Level var1, int var2, int var3, int var4) {
		return this.liquid == Liquid.lava ? 100.0F : var1.getBrightness(var2, var3, var4);
	}

	protected final boolean shouldRenderFace(Level var1, int var2, int var3, int var4, int var5, int var6) {
		if(var2 >= 0 && var3 >= 0 && var4 >= 0 && var2 < var1.width && var4 < var1.height) {
			if(var5 != 1 && this.liquid == Liquid.water) {
				return false;
			} else {
				var5 = var1.getTile(var2, var3, var4);
				return var5 != this.tileId && var5 != this.calmTileId ? (var6 != 1 || var1.getTile(var2 - 1, var3, var4) != 0 && var1.getTile(var2 + 1, var3, var4) != 0 && var1.getTile(var2, var3, var4 - 1) != 0 && var1.getTile(var2, var3, var4 + 1) != 0 ? super.shouldRenderFace(var1, var2, var3, var4, -1, var6) : true) : false;
			}
		} else {
			return false;
		}
	}

	public final void renderFace(Tesselator var1, int var2, int var3, int var4, int var5) {
		super.renderFace(var1, var2, var3, var4, var5);
		super.renderBackFace(var1, var2, var3, var4, var5);
	}

	public final boolean mayPick() {
		return false;
	}

	public final AABB getAABB(int var1, int var2, int var3) {
		return null;
	}

	public final boolean blocksLight() {
		return true;
	}

	public final boolean isSolid() {
		return false;
	}

	public final Liquid getLiquidType() {
		return this.liquid;
	}

	public void neighborChanged(Level var1, int var2, int var3, int var4, int var5) {
		if(var5 != 0) {
			Liquid var6 = Tile.tiles[var5].getLiquidType();
			if(this.liquid == Liquid.water && var6 == Liquid.lava || var6 == Liquid.water && this.liquid == Liquid.lava) {
				var1.setTile(var2, var3, var4, Tile.rock.id);
				return;
			}
		}

		var1.addToTickNextTick(var2, var3, var4, var5);
	}

	public final int getTickDelay() {
		return this.liquid == Liquid.lava ? 5 : 0;
	}
}
