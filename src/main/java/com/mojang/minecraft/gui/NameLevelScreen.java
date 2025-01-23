package com.mojang.minecraft.gui;

import com.mojang.minecraft.Minecraft;
import org.lwjgl.input.Keyboard;

public final class NameLevelScreen extends Screen {
	private Screen parent;
	private String title = "Enter level name:";
	private int id;
	private String name;
	private int counter = 0;

	public NameLevelScreen(Screen var1, String var2, int var3) {
		this.parent = var1;
		this.id = var3;
		this.name = var2;
		if(this.name.equals("-")) {
			this.name = "";
		}

	}

	public final void init() {
		this.buttons.clear();
		Keyboard.enableRepeatEvents(true);
		this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120, 200, 20, "Save"));
		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 144, 200, 20, "Cancel"));
		((Button)this.buttons.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void closeScreen() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void tick() {
		++this.counter;
	}

	protected final void buttonClicked(Button var1) {
		if(var1.enabled) {
			if(var1.id == 0 && this.name.trim().length() > 1) {
				Minecraft var10000 = this.minecraft;
				int var10001 = this.id;
				String var4 = this.name.trim();
				int var3 = var10001;
				Minecraft var2 = var10000;
				var2.levelIo.save(var2.level, var2.minecraftUri, var2.user.name, var2.user.sessionId, var4, var3);
				this.minecraft.setScreen((Screen)null);
				this.minecraft.grabMouse();
			}

			if(var1.id == 1) {
				this.minecraft.setScreen(this.parent);
			}

		}
	}

	protected final void keyPressed(char var1, int var2) {
		if(var2 == 14 && this.name.length() > 0) {
			this.name = this.name.substring(0, this.name.length() - 1);
		}

		if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(var1) >= 0 && this.name.length() < 64) {
			this.name = this.name + var1;
		}

		((Button)this.buttons.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void render(int var1, int var2) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		this.drawCenteredString(this.title, this.width / 2, 40, 16777215);
		int var3 = this.width / 2 - 100;
		int var4 = this.height / 2 - 10;
		fill(var3 - 1, var4 - 1, var3 + 200 + 1, var4 + 20 + 1, -6250336);
		fill(var3, var4, var3 + 200, var4 + 20, -16777216);
		this.drawString(this.name + (this.counter / 6 % 2 == 0 ? "_" : ""), var3 + 4, var4 + 6, 14737632);
		super.render(var1, var2);
	}
}
