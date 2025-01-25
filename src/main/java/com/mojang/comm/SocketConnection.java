package com.mojang.comm;

import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.net.Packet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public final class SocketConnection {
	public volatile boolean connected;
	//public SocketChannel socketChannel = SocketChannel.open();
	public ByteBuffer readBuffer = ByteBuffer.allocate(1048576);
	public ByteBuffer writeBuffer = ByteBuffer.allocate(1048576);
	public ConnectionManager manager;
	private Socket socket;
	private boolean initialized = false;
	private byte[] stringPacket = new byte[64];

	public SocketConnection(String var1, int var2) throws IOException {
		
	}

	public final void disconnect() {
	
	}

	public final void processData() throws IOException {
		
	}

	public Object read(Class var1) {
		return var1;
		
	}
}
