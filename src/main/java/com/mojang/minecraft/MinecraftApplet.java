package com.mojang.minecraft;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

public class MinecraftApplet extends Applet {
	private Canvas canvas;
	private Minecraft minecraft;
	private Thread thread = null;

	public void init() {
		this.canvas = new Canvas() {
			public final synchronized void addNotify() {
				super.addNotify();
				MinecraftApplet.this.startGameThread();
			}

			public final synchronized void removeNotify() {
				MinecraftApplet.this.stopGameThread();
				super.removeNotify();
			}
		};
		this.minecraft = new Minecraft(this.canvas, this.getWidth(), this.getHeight(), false);
		this.minecraft.minecraftUri = this.getDocumentBase().getHost();
		if(this.getDocumentBase().getPort() > 0) {
			this.minecraft.minecraftUri = this.minecraft.minecraftUri + ":" + this.getDocumentBase().getPort();
		}

		if(this.getParameter("username") != null && this.getParameter("sessionid") != null) {
			this.minecraft.user = new User(this.getParameter("username"), this.getParameter("sessionid"));
			if(this.getParameter("mppass") != null) {
				this.minecraft.user.mpPass = this.getParameter("mppass");
			}
		}

		if(this.getParameter("loadmap_user") != null && this.getParameter("loadmap_id") != null) {
			this.minecraft.loadMapUser = this.getParameter("loadmap_user");
			this.minecraft.loadMapId = Integer.parseInt(this.getParameter("loadmap_id"));
		} else if(this.getParameter("server") != null && this.getParameter("port") != null) {
			Minecraft var10000 = this.minecraft;
			String var10001 = this.getParameter("server");
			int var3 = Integer.parseInt(this.getParameter("port"));
			String var2 = var10001;
			Minecraft var1 = var10000;
			var1.server = var2;
			var1.port = var3;
		}

		this.minecraft.appletMode = true;
		this.setLayout(new BorderLayout());
		this.add(this.canvas, "Center");
		this.canvas.setFocusable(true);
		this.validate();
	}

	public void startGameThread() {
		if(this.thread == null) {
			this.thread = new Thread(this.minecraft);
			this.thread.start();
		}
	}

	public void start() {
		this.minecraft.pause = false;
	}

	public void stop() {
		this.minecraft.pause = true;
	}

	public void destroy() {
		this.stopGameThread();
	}

	public void stopGameThread() {
		if(this.thread != null) {
			Minecraft var1 = this.minecraft;
			var1.running = false;

			try {
				this.thread.join(1000L);
			} catch (InterruptedException var3) {
				try {
					this.minecraft.destroy();
				} catch (Exception var2) {
					var2.printStackTrace();
				}
			}

			this.thread = null;
		}
	}
}
