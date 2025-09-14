package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;

import net.lax1dude.eaglercraft.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.internal.PlatformNetworking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public final class ConnectionManager {
	public ByteArrayOutputStream levelBuffer;
	public SocketConnection connection;
	public Minecraft minecraft;
	public boolean processData = false;
	public HashMap players = new HashMap();
	private boolean loginSent = false;

	public ConnectionManager(Minecraft var1, String var2, String var4) throws IOException{
		this.connection = new SocketConnection(this);
		SocketConnection var5 = this.connection;
		var5.manager = this;
		this.processData = true;
		this.minecraft = var1;
		this.connection.webSocket = PlatformNetworking.openWebSocket(var2);

		if (this.connection.webSocket == null) {
			throw new IOException("Failed to open websocket to: " + var2);
		}
	}

	public final void sendBlockChange(int var1, int var2, int var3, int var4, int var5) {
		this.connection.sendPacket(Packet.PLACE_OR_REMOVE_TILE, new Object[]{Integer.valueOf(var1), Integer.valueOf(var2), Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5)});
	}

	public final void disconnect(Exception var1) {
		this.connection.disconnect();
		this.minecraft.setScreen(new ErrorScreen("Disconnected!", var1.getMessage()));
		var1.printStackTrace();
	}

	public final boolean isConnected() {
		if(this.connection != null) {
			SocketConnection var1 = this.connection;
			if(var1.webSocket != null) {
				if(var1.webSocket.getState() == EnumEaglerConnectionState.CONNECTED) {
					return true;
				}
			}
		}

		return false;
	}
	
    public void tick() {
        if (this.connection.webSocket != null
                && this.connection.webSocket.getState() == EnumEaglerConnectionState.CONNECTED
                && !loginSent) {
        	this.connection.sendPacket(Packet.LOGIN, new Object[]{Byte.valueOf((byte)6), this.minecraft.user.name, "", Integer.valueOf(0)});
        	loginSent = true;
        }
    }
}
