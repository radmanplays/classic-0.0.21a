package com.mojang.minecraft.gui;

import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.Packet;
import org.lwjgl.input.Keyboard;

public final class ChatScreen extends Screen {
	private String typedMsg = "";
	private int counter = 0;

	public final void init() {
		Keyboard.enableRepeatEvents(true);
	}

	public final void closeScreen() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void tick() {
		++this.counter;
	}

	protected final void keyPressed(char var1, int var2) {
		if(var2 == 1) {
			this.minecraft.setScreen((Screen)null);
		} else if(var2 == 28) {
			ConnectionManager var10000 = this.minecraft.connectionManager;
			String var4 = this.typedMsg.trim();
			ConnectionManager var3 = var10000;
			var4 = var4.trim();
			if(var4.length() > 0) {
				var3.connection.sendPacket(Packet.CHAT_MESSAGE, new Object[]{Integer.valueOf(-1), var4});
			}

			this.minecraft.setScreen((Screen)null);
		} else {
			if(var2 == 14 && this.typedMsg.length() > 0) {
				this.typedMsg = this.typedMsg.substring(0, this.typedMsg.length() - 1);
			}

			if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\\\"#%/()=+?[]{}<>@|$;".indexOf(var1) >= 0 && this.typedMsg.length() < 64 - (this.minecraft.user.name.length() + 2)) {
				this.typedMsg = this.typedMsg + var1;
			}

		}
	}

	public final void render(int var1, int var2) {
		fill(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		this.drawString("> " + this.typedMsg + (this.counter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
	}
}
