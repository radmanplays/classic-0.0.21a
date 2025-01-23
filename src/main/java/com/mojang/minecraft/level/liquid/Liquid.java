package com.mojang.minecraft.level.liquid;

public final class Liquid {
	private static Liquid[] liquids = new Liquid[4];
	public static final Liquid none = new Liquid(0);
	public static final Liquid water = new Liquid(1);
	public static final Liquid lava = new Liquid(2);

	private Liquid(int var1) {
		liquids[var1] = this;
	}
}
