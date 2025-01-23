package com.mojang.minecraft.gui;

public final class NewLevelScreen extends Screen {
	private Screen parent;

	public NewLevelScreen(Screen var1) {
		this.parent = var1;
	}

	public final void init() {
		this.buttons.clear();
		this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 3, 200, 20, "Small"));
		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 3 + 32, 200, 20, "Normal"));
		this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 3 + 64, 200, 20, "Huge"));
		this.buttons.add(new Button(3, this.width / 2 - 100, this.height / 3 + 96, 200, 20, "Cancel"));
	}

	protected final void buttonClicked(Button var1) {
		if(var1.id == 3) {
			this.minecraft.setScreen(this.parent);
		} else {
			this.minecraft.generateLevel(var1.id);
			this.minecraft.setScreen((Screen)null);
			this.minecraft.grabMouse();
		}
	}

	public final void render(int var1, int var2) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		this.drawCenteredString("Generate new level", this.width / 2, 40, 16777215);
		super.render(var1, var2);
	}
}
