package com.mojang.minecraft.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LoadLevelScreen extends Screen implements Runnable {
	private Screen parent;
	private boolean finished = false;
	private boolean loaded = false;
	private String[] levels = null;
	private String status = "";
	protected String title = "Load level";

	public LoadLevelScreen(Screen var1) {
		this.parent = var1;
	}

	public void run() {
		try {
			this.status = "Getting level list..";
			URL var1 = new URL("http://" + this.minecraft.minecraftUri + "/listmaps.jsp?user=" + this.minecraft.user.name);
			BufferedReader var3 = new BufferedReader(new InputStreamReader(var1.openConnection().getInputStream()));
			this.levels = var3.readLine().split(";");
			if(this.levels.length >= 5) {
				this.setLevels(this.levels);
				this.loaded = true;
				return;
			}

			this.status = this.levels[0];
			this.finished = true;
		} catch (Exception var2) {
			var2.printStackTrace();
			this.status = "Failed to load levels";
			this.finished = true;
		}

	}

	protected void setLevels(String[] var1) {
		for(int var2 = 0; var2 < 5; ++var2) {
			((Button)this.buttons.get(var2)).enabled = !var1[var2].equals("-");
			((Button)this.buttons.get(var2)).msg = var1[var2];
			((Button)this.buttons.get(var2)).visible = true;
		}

	}

	public final void init() {
		(new Thread(this)).start();

		for(int var1 = 0; var1 < 5; ++var1) {
			this.buttons.add(new Button(var1, this.width / 2 - 100, this.height / 4 + var1 * 24, 200, 20, "---"));
			((Button)this.buttons.get(var1)).visible = false;
		}

		this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 4 + 144, 200, 20, "Cancel"));
	}

	protected final void buttonClicked(Button var1) {
		if(var1.enabled) {
			if(this.loaded && var1.id < 5) {
				this.loadLevel(var1.id);
			}

			if(this.finished || this.loaded && var1.id == 5) {
				this.minecraft.setScreen(this.parent);
			}

		}
	}

	protected void loadLevel(int var1) {
		this.minecraft.loadLevel(this.minecraft.user.name, var1);
		this.minecraft.setScreen((Screen)null);
		this.minecraft.grabMouse();
	}

	public final void render(int var1, int var2) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		this.drawCenteredString(this.title, this.width / 2, 40, 16777215);
		if(!this.loaded) {
			this.drawCenteredString(this.status, this.width / 2, this.height / 2 - 4, 16777215);
		}

		super.render(var1, var2);
	}
}
