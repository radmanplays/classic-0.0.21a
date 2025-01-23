package com.mojang.minecraft.gui;

public final class ErrorScreen extends Screen {
	private String title;
	private String desc;

	public ErrorScreen(String var1, String var2) {
		this.title = var1;
		this.desc = var2;
	}

	public final void init() {
	}

	public final void render(int var1, int var2) {
		fillGradient(0, 0, this.width, this.height, -12574688, -11530224);
		this.drawCenteredString(this.title, this.width / 2, 90, 16777215);
		this.drawCenteredString(this.desc, this.width / 2, 110, 16777215);
		super.render(var1, var2);
	}

	protected final void keyPressed(char var1, int var2) {
	}
}
