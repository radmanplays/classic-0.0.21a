package com.mojang.minecraft.level;

import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.mojang.minecraft.Minecraft;

public final class LevelIO {
	private Minecraft a;

	public LevelIO(Minecraft var1) {
		this.a = var1;
	}

	public final boolean save(Level var1, String var2, String var3, String var4, String var5, int var6) {
		// if(var4 == null) {
		// 	var4 = "";
		// }

		// if(this.a != null && this.a != null) {
		// 	this.a.beginLevelLoading("Saving level");
		// }

		// try {
		// 	if(this.a != null && this.a != null) {
		// 		this.a.levelLoadUpdate("Compressing..");
		// 	}

		// 	ByteArrayOutputStream var7 = new ByteArrayOutputStream();
		// 	save(var1, var7);
		// 	var7.close();
		// 	byte[] var10 = var7.toByteArray();
		// 	if(this.a != null && this.a != null) {
		// 		this.a.levelLoadUpdate("Connecting..");
		// 	}

		// 	URL var12 = new URL("http://" + var2 + "/level/save.html");
		// 	HttpURLConnection var13 = (HttpURLConnection)var12.openConnection();
		// 	var13.setDoInput(true);
		// 	var13.setDoOutput(true);
		// 	var13.setRequestMethod("POST");
		// 	DataOutputStream var14 = new DataOutputStream(var13.getOutputStream());
		// 	var14.writeUTF(var3);
		// 	var14.writeUTF(var4);
		// 	var14.writeUTF(var5);
		// 	var14.writeByte(var6);
		// 	var14.writeInt(var10.length);
		// 	if(this.a != null) {
		// 		this.a.levelLoadUpdate("Saving..");
		// 	}

		// 	var14.write(var10);
		// 	var14.close();
		// 	BufferedReader var11 = new BufferedReader(new InputStreamReader(var13.getInputStream()));
		// 	var2 = var11.readLine();
		// 	if(!var2.equalsIgnoreCase("ok")) {
		// 		if(this.a != null) {
		// 			this.a.levelLoadUpdate("Failed: " + var11.readLine());
		// 		}

		// 		var11.close();
		// 		Thread.sleep(1000L);
		// 		return false;
		// 	} else {
		// 		var11.close();
		// 		return true;
		// 	}
		// } catch (Exception var9) {
		// 	var9.printStackTrace();
		// 	if(this.a != null) {
		// 		this.a.levelLoadUpdate("Failed!");
		// 	}

		// 	try {
		// 		Thread.sleep(1000L);
		// 	} catch (InterruptedException var8) {
		// 	}

		// 	return false;
		// }
		return false;
	}

	public final Level load(String var1, String var2, int var3) {
		// if(this.a != null) {
		// 	this.a.beginLevelLoading("Loading level");
		// }

		// try {
		// 	if(this.a != null) {
		// 		this.a.levelLoadUpdate("Connecting..");
		// 	}

		// 	URL var6 = new URL("http://" + var1 + "/level/load.html?id=" + var3 + "&user=" + var2);
		// 	HttpURLConnection var7 = (HttpURLConnection)var6.openConnection();
		// 	var7.setDoInput(true);
		// 	if(this.a != null) {
		// 		this.a.levelLoadUpdate("Loading..");
		// 	}

		// 	DataInputStream var8 = new DataInputStream(var7.getInputStream());
		// 	var2 = var8.readUTF();
		// 	if(var2.equalsIgnoreCase("ok")) {
		// 		return this.load(var8);
		// 	} else {
		// 		if(this.a != null) {
		// 			this.a.levelLoadUpdate("Failed: " + var8.readUTF());
		// 		}

		// 		var8.close();
		// 		Thread.sleep(1000L);
		// 		return null;
		// 	}
		// } catch (Exception var5) {
		// 	var5.printStackTrace();
		// 	if(this.a != null) {
		// 		this.a.levelLoadUpdate("Failed!");
		// 	}

		// 	try {
		// 		Thread.sleep(3000L);
		// 	} catch (InterruptedException var4) {
		// 	}

		// 	return null;
		// }
		return null;
	}

	public final Level load(InputStream var1) {
		// if(this.a != null) {
		// 	this.a.beginLevelLoading("Loading level");
		// }

		// if(this.a != null) {
		// 	this.a.levelLoadUpdate("Reading..");
		// }

		// try {
		// 	DataInputStream var10 = new DataInputStream(new GZIPInputStream(var1));
		// 	int var12 = var10.readInt();
		// 	if(var12 != 656127880) {
		// 		return null;
		// 	} else {
		// 		byte var13 = var10.readByte();
		// 		if(var13 > 2) {
		// 			return null;
		// 		} else if(var13 <= 1) {
		// 			System.out.println("Version is 1!");
		// 			String var15 = var10.readUTF();
		// 			String var16 = var10.readUTF();
		// 			long var7 = var10.readLong();
		// 			short var3 = var10.readShort();
		// 			short var4 = var10.readShort();
		// 			short var5 = var10.readShort();
		// 			byte[] var6 = new byte[var3 * var4 * var5];
		// 			var10.readFully(var6);
		// 			var10.close();
		// 			Level var11 = new Level();
		// 			var11.setData(var3, var5, var4, var6);
		// 			var11.name = var15;
		// 			var11.creator = var16;
		// 			var11.createTime = var7;
		// 			return var11;
		// 		} else {
		// 			ObjectInputStream var14 = new ObjectInputStream(var10);
		// 			Level var2 = (Level)var14.readObject();
		// 			var2.initTransient();
		// 			var14.close();
		// 			return var2;
		// 		}
		// 	}
		// } catch (Exception var9) {
		// 	var9.printStackTrace();
		// 	(new StringBuilder()).append("Failed to load level: ").append(var9.toString()).toString();
		// 	return null;
		// }
		return null;
	}

	public final Level loadLegacy(InputStream var1) {
		// if(this.a != null) {
		// 	this.a.beginLevelLoading("Loading level");
		// }

		// if(this.a != null) {
		// 	this.a.levelLoadUpdate("Reading..");
		// }

		// try {
		// 	DataInputStream var5 = new DataInputStream(new GZIPInputStream(var1));
		// 	String var7 = "--";
		// 	String var2 = "unknown";
		// 	byte[] var3 = new byte[256 << 8 << 6];
		// 	var5.readFully(var3);
		// 	var5.close();
		// 	Level var6 = new Level();
		// 	var6.setData(256, 64, 256, var3);
		// 	var6.name = var7;
		// 	var6.creator = var2;
		// 	var6.createTime = 0L;
		// 	return var6;
		// } catch (Exception var4) {
		// 	var4.printStackTrace();
		// 	(new StringBuilder()).append("Failed to load level: ").append(var4.toString()).toString();
		// 	return null;
		// }
		return null;
	}

	public static void save(Level var0, OutputStream var1) {
		// try {
		// 	DataOutputStream var3 = new DataOutputStream(new GZIPOutputStream(var1));
		// 	var3.writeInt(656127880);
		// 	var3.writeByte(2);
		// 	ObjectOutputStream var4 = new ObjectOutputStream(var3);
		// 	var4.writeObject(var0);
		// 	var4.close();
		// } catch (Exception var2) {
		// 	var2.printStackTrace();
		// }
	}

	public static byte[] loadBlocks(InputStream var0) {
		// try {
		// 	DataInputStream var3 = new DataInputStream(new GZIPInputStream(var0));
		// 	byte[] var1 = new byte[var3.readInt()];
		// 	var3.readFully(var1);
		// 	var3.close();
		// 	return var1;
		// } catch (Exception var2) {
		// 	throw new RuntimeException(var2);
		// }
		return null;
	}
}