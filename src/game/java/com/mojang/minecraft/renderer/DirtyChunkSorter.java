package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;
import java.util.Comparator;

public final class DirtyChunkSorter implements Comparator {
	private Player player;

	public DirtyChunkSorter(Player var1) {
		this.player = var1;
	}

	public final int compare(Object var1, Object var2) {
		Chunk var10001 = (Chunk)var1;
		Chunk var6 = (Chunk)var2;
		Chunk var5 = var10001;
		boolean var3 = var5.isInFrustum;
		boolean var4 = var6.isInFrustum;
		return var3 && !var4 ? -1 : ((!var4 || var3) && var5.a(this.player) < var6.a(this.player) ? -1 : 1);
	}
}
