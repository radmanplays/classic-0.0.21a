package com.mojang.minecraft.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.renderer.texture.TextureFX;

import net.lax1dude.eaglercraft.adapter.RealOpenGLEnums;

public class Textures {
	private HashMap idMap = new HashMap();
	public IntBuffer idBuffer = BufferUtils.createIntBuffer(1);
	public ByteBuffer textureBuffer = BufferUtils.createByteBuffer(262144);
	public List textureList = new ArrayList();

	public final int getTextureId(String var1) {
		try {
			if(this.idMap.containsKey(var1)) {
				return ((Integer)this.idMap.get(var1)).intValue();
			} else {
				int var2 = this.addTexture(ImageIO.read(Textures.class.getResourceAsStream(var1)));
				this.idMap.put(var1, Integer.valueOf(var2));
				return var2;
			}
		} catch (IOException var3) {
			throw new RuntimeException("!!");
		}
	}

	public final int addTexture(BufferedImage var1) {
		this.idBuffer.clear();
		GL11.glGenTextures(this.idBuffer);
		int var2 = this.idBuffer.get(0);
		GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, var2);
		GL11.glTexParameteri(RealOpenGLEnums.GL_TEXTURE_2D, RealOpenGLEnums.GL_TEXTURE_MIN_FILTER, RealOpenGLEnums.GL_NEAREST);
		GL11.glTexParameteri(RealOpenGLEnums.GL_TEXTURE_2D, RealOpenGLEnums.GL_TEXTURE_MAG_FILTER, RealOpenGLEnums.GL_NEAREST);
		int var3 = var1.getWidth();
		int var4 = var1.getHeight();
		int[] var5 = new int[var3 * var4];
		byte[] var6 = new byte[var3 * var4 << 2];
		var1.getRGB(0, 0, var3, var4, var5, 0, var3);

		for(int var11 = 0; var11 < var5.length; ++var11) {
			int var7 = var5[var11] >>> 24;
			int var8 = var5[var11] >> 16 & 255;
			int var9 = var5[var11] >> 8 & 255;
			int var10 = var5[var11] & 255;
			var6[var11 << 2] = (byte)var8;
			var6[(var11 << 2) + 1] = (byte)var9;
			var6[(var11 << 2) + 2] = (byte)var10;
			var6[(var11 << 2) + 3] = (byte)var7;
		}

		this.textureBuffer.clear();
		this.textureBuffer.put(var6);
		this.textureBuffer.position(0).limit(var6.length);
		GL11.glTexImage2D(RealOpenGLEnums.GL_TEXTURE_2D, 0, RealOpenGLEnums.GL_RGBA, var3, var4, 0, RealOpenGLEnums.GL_RGBA, RealOpenGLEnums.GL_UNSIGNED_BYTE, (ByteBuffer)this.textureBuffer);
		return var2;
	}

	public final void registerTextureFX(TextureFX var1) {
		this.textureList.add(var1);
		var1.onTick();
	}
}
