package com.mojang.minecraft.character;

public final class Polygon {
	public Vertex[] vertices;

	private Polygon(Vertex[] var1) {
		this.vertices = var1;
	}

	public Polygon(Vertex[] var1, int var2, int var3, int var4, int var5) {
		this(var1);
		var1[0] = var1[0].remap((float)var4, (float)var3);
		var1[1] = var1[1].remap((float)var2, (float)var3);
		var1[2] = var1[2].remap((float)var2, (float)var5);
		var1[3] = var1[3].remap((float)var4, (float)var5);
	}
}
