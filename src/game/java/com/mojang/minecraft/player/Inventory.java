package com.mojang.minecraft.player;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;

public final class Inventory {
	public int[] slots = new int[9];
	public int selectedSlot = 0;

	public Inventory() {
		for(int var1 = 0; var1 < 9; ++var1) {
			this.slots[var1] = ((Tile)User.creativeTiles.get(var1)).id;
		}

	}

	public final int getSelected() {
		return this.slots[this.selectedSlot];
	}

	public int getSlotContainsID(int var1) {
		for(int var2 = 0; var2 < this.slots.length; ++var2) {
			if(var1 == this.slots[var2]) {
				return var2;
			}
		}

		return -1;
	}

	public final void getSlotContainsTile(Tile var1) {
		if(var1 != null) {
			int var2 = this.getSlotContainsID(var1.id);
			if(var2 >= 0) {
				this.slots[var2] = this.slots[this.selectedSlot];
			}

			this.slots[this.selectedSlot] = var1.id;
		}

	}
}
