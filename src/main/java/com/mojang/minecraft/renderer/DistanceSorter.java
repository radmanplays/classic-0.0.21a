package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;
import java.util.Comparator;

public final class DistanceSorter implements Comparator {
	private Player player;

	public DistanceSorter(Player var1) {
		this.player = var1;
	}

	public final int compare(Object var1, Object var2) {
		Chunk var10001 = (Chunk)var1;
		Chunk var4 = (Chunk)var2;
		Chunk var3 = var10001;
		return var3.a(this.player) < var4.a(this.player) ? -1 : 1;
	}
}
