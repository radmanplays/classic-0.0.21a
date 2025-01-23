package com.mojang.minecraft.gui;

public final class SaveLevelScreen extends LoadLevelScreen {
	public SaveLevelScreen(Screen var1) {
		super(var1);
		this.title = "Save level";
	}

	protected final void setLevels(String[] var1) {
		for(int var2 = 0; var2 < 5; ++var2) {
			((Button)this.buttons.get(var2)).msg = var1[var2];
			((Button)this.buttons.get(var2)).visible = true;
		}

	}

	protected final void loadLevel(int var1) {
		this.minecraft.setScreen(new NameLevelScreen(this, ((Button)this.buttons.get(var1)).msg, var1));
	}
}
