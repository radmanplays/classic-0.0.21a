package com.mojang.minecraft.level;

import com.mojang.minecraft.Minecraft;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class LevelIO {
	private Minecraft minecraft;

	public LevelIO(Minecraft var1) {
		this.minecraft = var1;
	}

	public final Level load(VFile2 var1) {
		if(this.minecraft != null) {
			this.minecraft.beginLevelLoading("Loading level");
		}

		if(this.minecraft != null) {
			this.minecraft.levelLoadUpdate("Reading..");
		}

		try {
			DataInputStream var10 = new DataInputStream(new GZIPInputStream(var1.getInputStream()));
			int var12 = var10.readInt();
			if(var12 != 656127880) {
				return null;
			} else {
				byte var13 = var10.readByte();
				if(var13 > 2) {
					return null;
				} else if(var13 <= 1) {
					System.out.println("Version is 1!");
					String var15 = var10.readUTF();
					String var16 = var10.readUTF();
					long var7 = var10.readLong();
					short var3 = var10.readShort();
					short var4 = var10.readShort();
					short var5 = var10.readShort();
					byte[] var6 = new byte[var3 * var4 * var5];
					var10.readFully(var6);
					var10.close();
					Level var11 = new Level();
					var11.setData(var3, var5, var4, var6);
					var11.name = var15;
					var11.creator = var16;
					var11.createTime = var7;
					return var11;
				} else {
					Level var2 = new Level();
					var2.readFrom(var10, var13);
					var2.initTransient();
					var10.close();
					return var2;
				}
			}
		} catch (Exception var9) {
			var9.printStackTrace();
			(new StringBuilder()).append("Failed to load level: ").append(var9.toString()).toString();
			return null;
		}
	}

	public final Level loadLegacy(VFile2 var1) {
		if(this.minecraft != null) {
			this.minecraft.beginLevelLoading("Loading level");
		}

		if(this.minecraft != null) {
			this.minecraft.levelLoadUpdate("Reading..");
		}

		try {
			DataInputStream var5 = new DataInputStream(new GZIPInputStream(var1.getInputStream()));
			String var7 = "--";
			String var2 = "unknown";
			byte[] var3 = new byte[256 << 8 << 6];
			var5.readFully(var3);
			var5.close();
			Level var6 = new Level();
			var6.setData(256, 64, 256, var3);
			var6.name = var7;
			var6.creator = var2;
			var6.createTime = 0L;
			return var6;
		} catch (Exception var4) {
			var4.printStackTrace();
			(new StringBuilder()).append("Failed to load level: ").append(var4.toString()).toString();
			return null;
		}
	}

	public static void save(Level var0, VFile2 var1) {
		try (DataOutputStream var3 = new DataOutputStream(new GZIPOutputStream(var1.getOutputStream()))) {
			var3.writeInt(656127880);
			var3.writeByte(2);
			var0.writeTo(var3);
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}
	
	public static byte[] loadBlocks(InputStream var0) {
		try {
			DataInputStream var3 = new DataInputStream(new GZIPInputStream(var0));
			byte[] var1 = new byte[var3.readInt()];
			var3.readFully(var1);
			var3.close();
			return var1;
		} catch (Exception var2) {
			throw new RuntimeException(var2);
		}
	}
}
