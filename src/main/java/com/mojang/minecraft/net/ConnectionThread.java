package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;
import java.io.IOException;

final class ConnectionThread extends Thread {
	private String ip;
	private int port;
	private String username;
	private String mpPass;
	private Minecraft minecraft;
	private ConnectionManager connectionManager;

	ConnectionThread(ConnectionManager var1, String var2, int var3, String var4, String var5, Minecraft var6) {
		this.connectionManager = var1;
		this.ip = var2;
		this.port = var3;
		this.username = var4;
		this.mpPass = var5;
		this.minecraft = var6;
	}

	public final void run() {
		try {
			ConnectionManager var10000 = this.connectionManager;
			SocketConnection var2 = new SocketConnection(this.ip, this.port);
			ConnectionManager var1 = var10000;
			var1.connection = var2;
			var1 = this.connectionManager;
			ConnectionManager var5 = this.connectionManager;
			SocketConnection var4 = var1.connection;
			var4.manager = var5;
			var1 = this.connectionManager;
			//var1.connection.sendPacket(Packet.LOGIN, new Object[]{Byte.valueOf((byte)6), this.username, this.mpPass, Integer.valueOf(0)});
			boolean var6 = true;
			var1 = this.connectionManager;
			var1.processData = true;
		} catch (IOException var3) {
			this.minecraft.hideGui = false;
			this.minecraft.connectionManager = null;
			this.minecraft.setScreen(new ErrorScreen("Failed to connect", "You failed to connect to the server. It\'s probably down!"));
		}
	}
}
