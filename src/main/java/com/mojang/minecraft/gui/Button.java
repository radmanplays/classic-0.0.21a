package com.mojang.minecraft.gui;

public final class Button {
	public int x;
	public int y;
	public int w;
	public int h;
	public String msg;
	public int id;
	public boolean enabled = true;
	public boolean visible = true;

	public Button(int var1, int var2, int var3, int var4, int var5, String var6) {
		this.id = var1;
		this.x = var2;
		this.y = var3;
		this.w = 200;
		this.h = 20;
		this.msg = var6;
	}
}
