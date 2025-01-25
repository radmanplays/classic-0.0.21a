package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public final class ConnectionManager {
	public ByteArrayOutputStream levelBuffer;
	public SocketConnection connection;
	public Minecraft minecraft;
	public boolean processData = false;
	public boolean connected = false;
	public HashMap players = new HashMap();

	public ConnectionManager(Minecraft var1, String var2, int var3, String var4, String var5) {
		var1.hideGui = true;
		this.minecraft = var1;
		(new ConnectionThread(this, var2, var3, var4, var5, var1)).start();
	}

	public final void sendBlockChange(int var1, int var2, int var3, int var4, int var5) {
		//this.connection.sendPacket(Packet.PLACE_OR_REMOVE_TILE, new Object[]{Integer.valueOf(var1), Integer.valueOf(var2), Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5)});
	}

	public final void disconnect(Exception var1) {
		this.connection.disconnect();
		this.minecraft.setScreen(new ErrorScreen("Disconnected!", var1.getMessage()));
		var1.printStackTrace();
	}

	public final boolean isConnected() {
		if(this.connection != null) {
			SocketConnection var1 = this.connection;
			if(var1.connected) {
				return true;
			}
		}

		return false;
	}
}
