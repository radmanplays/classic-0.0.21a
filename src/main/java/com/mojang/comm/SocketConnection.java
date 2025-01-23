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
	public SocketChannel socketChannel = SocketChannel.open();
	public ByteBuffer readBuffer = ByteBuffer.allocate(1048576);
	public ByteBuffer writeBuffer = ByteBuffer.allocate(1048576);
	public ConnectionManager manager;
	private Socket socket;
	private boolean initialized = false;
	private byte[] stringPacket = new byte[64];

	public SocketConnection(String var1, int var2) throws IOException {
		this.socketChannel.connect(new InetSocketAddress(var1, var2));
		this.socketChannel.configureBlocking(false);
		System.currentTimeMillis();
		this.socket = this.socketChannel.socket();
		this.connected = true;
		this.readBuffer.clear();
		this.writeBuffer.clear();
		this.socket.setTcpNoDelay(true);
		this.socket.setTrafficClass(24);
		this.socket.setKeepAlive(false);
		this.socket.setReuseAddress(false);
		this.socket.setSoTimeout(100);
		this.socket.getInetAddress().toString();
	}

	public final void disconnect() {
		try {
			if(this.writeBuffer.position() > 0) {
				this.writeBuffer.flip();
				this.socketChannel.write(this.writeBuffer);
				this.writeBuffer.compact();
			}
		} catch (Exception var2) {
		}

		this.connected = false;

		try {
			this.socketChannel.close();
		} catch (Exception var1) {
		}

		this.socket = null;
		this.socketChannel = null;
	}

	public final void processData() throws IOException {
		this.socketChannel.read(this.readBuffer);
		int var1 = 0;

		while(this.readBuffer.position() > 0 && var1++ != 100) {
			this.readBuffer.flip();
			byte var2 = this.readBuffer.get(0);
			Packet var3 = Packet.PACKETS[var2];
			if(var3 == null) {
				throw new IOException("Bad command: " + var2);
			}

			if(this.readBuffer.remaining() < var3.size + 1) {
				this.readBuffer.compact();
				break;
			}

			this.readBuffer.get();
			Object[] var11 = new Object[var3.fields.length];

			for(int var4 = 0; var4 < var11.length; ++var4) {
				var11[var4] = this.read(var3.fields[var4]);
			}

			ConnectionManager var12 = this.manager;
			if(var12.processData) {
				if(var3 == Packet.LOGIN) {
					var12.minecraft.beginLevelLoading(var11[1].toString());
					var12.minecraft.levelLoadUpdate(var11[2].toString());
					var12.minecraft.player.userType = ((Byte)var11[3]).byteValue();
				} else if(var3 == Packet.LEVEL_INITIALIZE) {
					var12.minecraft.setLevel((Level)null);
					var12.levelBuffer = new ByteArrayOutputStream();
				} else {
					byte var6;
					if(var3 == Packet.LEVEL_DATA_CHUNK) {
						short var13 = ((Short)var11[0]).shortValue();
						byte[] var5 = (byte[])((byte[])var11[1]);
						var6 = ((Byte)var11[2]).byteValue();
						var12.minecraft.setLoadingProgress(var6);
						var12.levelBuffer.write(var5, 0, var13);
					} else {
						short var17;
						short var18;
						short var21;
						if(var3 == Packet.LEVEL_FINALIZE) {
							try {
								var12.levelBuffer.close();
							} catch (IOException var10) {
								var10.printStackTrace();
							}

							byte[] var14 = LevelIO.loadBlocks(new ByteArrayInputStream(var12.levelBuffer.toByteArray()));
							var12.levelBuffer = null;
							var18 = ((Short)var11[0]).shortValue();
							var21 = ((Short)var11[1]).shortValue();
							var17 = ((Short)var11[2]).shortValue();
							Level var7 = new Level();
							var7.setNetworkMode(true);
							var7.setData(var18, var21, var17, var14);
							var12.minecraft.setLevel(var7);
							var12.minecraft.hideGui = false;
							var12.connected = true;
						} else if(var3 == Packet.SET_TILE) {
							if(var12.minecraft.level != null) {
								var12.minecraft.level.netSetTile(((Short)var11[0]).shortValue(), ((Short)var11[1]).shortValue(), ((Short)var11[2]).shortValue(), ((Byte)var11[3]).byteValue());
							}
						} else {
							byte var8;
							byte var15;
							byte var10001;
							short var10003;
							String var19;
							short var10004;
							NetworkPlayer var20;
							if(var3 == Packet.PLAYER_JOIN) {
								var10001 = ((Byte)var11[0]).byteValue();
								String var10002 = (String)var11[1];
								var10003 = ((Short)var11[2]).shortValue();
								var10004 = ((Short)var11[3]).shortValue();
								short var10005 = ((Short)var11[4]).shortValue();
								byte var10006 = ((Byte)var11[5]).byteValue();
								byte var9 = ((Byte)var11[6]).byteValue();
								var8 = var10006;
								short var24 = var10005;
								var21 = var10004;
								var18 = var10003;
								var19 = var10002;
								var15 = var10001;
								if(var15 >= 0) {
									var20 = new NetworkPlayer(var12.minecraft, var15, var19, var18, var21, var24, (float)(-var8 * 360) / 256.0F, (float)(var9 * 360) / 256.0F);
									var12.players.put(Byte.valueOf(var15), var20);
									var12.minecraft.level.entities.add(var20);
								} else {
									var12.minecraft.level.setSpawnPos(var18 / 32, var21 / 32, var24 / 32, (float)(var8 * 320 / 256));
									var12.minecraft.player.moveTo((float)var18 / 32.0F, (float)var21 / 32.0F, (float)var24 / 32.0F, (float)(var8 * 360) / 256.0F, (float)(var9 * 360) / 256.0F);
								}
							} else {
								byte var25;
								NetworkPlayer var28;
								byte var33;
								if(var3 == Packet.PLAYER_TELEPORT) {
									var10001 = ((Byte)var11[0]).byteValue();
									short var29 = ((Short)var11[1]).shortValue();
									var10003 = ((Short)var11[2]).shortValue();
									var10004 = ((Short)var11[3]).shortValue();
									var33 = ((Byte)var11[4]).byteValue();
									var8 = ((Byte)var11[5]).byteValue();
									var25 = var33;
									var21 = var10004;
									var18 = var10003;
									var17 = var29;
									var15 = var10001;
									if(var15 < 0) {
										var12.minecraft.player.moveTo((float)var17 / 32.0F, (float)var18 / 32.0F, (float)var21 / 32.0F, (float)(var25 * 360) / 256.0F, (float)(var8 * 360) / 256.0F);
									} else {
										var28 = (NetworkPlayer)var12.players.get(Byte.valueOf(var15));
										if(var28 != null) {
											var28.teleport(var17, var18, var21, (float)(-var25 * 360) / 256.0F, (float)(var8 * 360) / 256.0F);
										}
									}
								} else {
									byte var22;
									byte var23;
									byte var30;
									byte var31;
									if(var3 == Packet.PLAYER_MOVE_AND_ROTATE) {
										var10001 = ((Byte)var11[0]).byteValue();
										var30 = ((Byte)var11[1]).byteValue();
										var31 = ((Byte)var11[2]).byteValue();
										byte var32 = ((Byte)var11[3]).byteValue();
										var33 = ((Byte)var11[4]).byteValue();
										var8 = ((Byte)var11[5]).byteValue();
										var25 = var33;
										var6 = var32;
										var22 = var31;
										var23 = var30;
										var15 = var10001;
										if(var15 >= 0) {
											var28 = (NetworkPlayer)var12.players.get(Byte.valueOf(var15));
											if(var28 != null) {
												var28.queue(var23, var22, var6, (float)(-var25 * 360) / 256.0F, (float)(var8 * 360) / 256.0F);
											}
										}
									} else if(var3 == Packet.PLAYER_ROTATE) {
										var10001 = ((Byte)var11[0]).byteValue();
										var30 = ((Byte)var11[1]).byteValue();
										var22 = ((Byte)var11[2]).byteValue();
										var23 = var30;
										var15 = var10001;
										if(var15 >= 0) {
											NetworkPlayer var26 = (NetworkPlayer)var12.players.get(Byte.valueOf(var15));
											if(var26 != null) {
												var26.queue((float)(-var23 * 360) / 256.0F, (float)(var22 * 360) / 256.0F);
											}
										}
									} else if(var3 == Packet.PLAYER_MOVE) {
										var10001 = ((Byte)var11[0]).byteValue();
										var30 = ((Byte)var11[1]).byteValue();
										var31 = ((Byte)var11[2]).byteValue();
										var6 = ((Byte)var11[3]).byteValue();
										var22 = var31;
										var23 = var30;
										var15 = var10001;
										if(var15 >= 0) {
											NetworkPlayer var27 = (NetworkPlayer)var12.players.get(Byte.valueOf(var15));
											if(var27 != null) {
												var27.queue(var23, var22, var6);
											}
										}
									} else if(var3 == Packet.PLAYER_DISCONNECT) {
										var15 = ((Byte)var11[0]).byteValue();
										if(var15 >= 0) {
											var20 = (NetworkPlayer)var12.players.remove(Byte.valueOf(var15));
											if(var20 != null) {
												var20.clear();
												var12.minecraft.level.entities.remove(var20);
											}
										}
									} else if(var3 == Packet.CHAT_MESSAGE) {
										var10001 = ((Byte)var11[0]).byteValue();
										var19 = (String)var11[1];
										var15 = var10001;
										if(var15 < 0) {
											var12.minecraft.addChatMessage("&e" + var19);
										} else {
											var12.players.get(Byte.valueOf(var15));
											var12.minecraft.addChatMessage(var19);
										}
									} else if(var3 == Packet.KICK_PLAYER) {
										var12.minecraft.setScreen(new ErrorScreen("Connection lost", (String)var11[0]));
									}
								}
							}
						}
					}
				}
			}

			if(!this.connected) {
				break;
			}

			this.readBuffer.compact();
		}

		if(this.writeBuffer.position() > 0) {
			this.writeBuffer.flip();
			this.socketChannel.write(this.writeBuffer);
			this.writeBuffer.compact();
		}

	}

	public final void sendPacket(Packet var1, Object... var2) {
		if(this.connected) {
			this.writeBuffer.put(var1.id);

			for(int var3 = 0; var3 < var2.length; ++var3) {
				Class var10001 = var1.fields[var3];
				Object var6 = var2[var3];
				Class var5 = var10001;
				SocketConnection var4 = this;
				if(this.connected) {
					try {
						if(var5 == Long.TYPE) {
							var4.writeBuffer.putLong(((Long)var6).longValue());
						} else if(var5 == Integer.TYPE) {
							var4.writeBuffer.putInt(((Number)var6).intValue());
						} else if(var5 == Short.TYPE) {
							var4.writeBuffer.putShort(((Number)var6).shortValue());
						} else if(var5 == Byte.TYPE) {
							var4.writeBuffer.put(((Number)var6).byteValue());
						} else if(var5 == Double.TYPE) {
							var4.writeBuffer.putDouble(((Double)var6).doubleValue());
						} else if(var5 == Float.TYPE) {
							var4.writeBuffer.putFloat(((Float)var6).floatValue());
						} else {
							byte[] var8;
							if(var5 != String.class) {
								if(var5 == byte[].class) {
									var8 = (byte[])((byte[])var6);
									if(var8.length < 1024) {
										var8 = Arrays.copyOf(var8, 1024);
									}

									var4.writeBuffer.put(var8);
								}
							} else {
								var8 = ((String)var6).getBytes("UTF-8");
								Arrays.fill(var4.stringPacket, (byte)32);

								int var9;
								for(var9 = 0; var9 < 64 && var9 < var8.length; ++var9) {
									var4.stringPacket[var9] = var8[var9];
								}

								for(var9 = var8.length; var9 < 64; ++var9) {
									var4.stringPacket[var9] = 32;
								}

								var4.writeBuffer.put(var4.stringPacket);
							}
						}
					} catch (Exception var7) {
						this.manager.disconnect(var7);
					}
				}
			}

		}
	}

	public Object read(Class var1) {
		if(!this.connected) {
			return null;
		} else {
			try {
				if(var1 == Long.TYPE) {
					return Long.valueOf(this.readBuffer.getLong());
				} else if(var1 == Integer.TYPE) {
					return Integer.valueOf(this.readBuffer.getInt());
				} else if(var1 == Short.TYPE) {
					return Short.valueOf(this.readBuffer.getShort());
				} else if(var1 == Byte.TYPE) {
					return Byte.valueOf(this.readBuffer.get());
				} else if(var1 == Double.TYPE) {
					return Double.valueOf(this.readBuffer.getDouble());
				} else if(var1 == Float.TYPE) {
					return Float.valueOf(this.readBuffer.getFloat());
				} else if(var1 == String.class) {
					this.readBuffer.get(this.stringPacket);
					return (new String(this.stringPacket, "UTF-8")).trim();
				} else if(var1 == byte[].class) {
					byte[] var3 = new byte[1024];
					this.readBuffer.get(var3);
					return var3;
				} else {
					return null;
				}
			} catch (Exception var2) {
				this.manager.disconnect(var2);
				return null;
			}
		}
	}
}
