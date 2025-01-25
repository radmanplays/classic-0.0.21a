package com.mojang.minecraft.renderer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.demez.minecraft.Client.GLAllocation;
import com.mojang.minecraft.renderer.texture.TextureFX;

import net.PeytonPlayz585.opengl.LWJGLMain;
import net.PeytonPlayz585.opengl.MinecraftImageData;

public class Textures {
	public static HashMap<String, Integer> textureMap = new HashMap<String, Integer>();
	private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
	public ByteBuffer textureBuffer = BufferUtils.createByteBuffer(262144);
	public List textureList = new ArrayList();

	public final int getTextureId(String var1) {
		Integer integer = (Integer) textureMap.get(var1);
		if (integer != null) {
			return integer.intValue();
		}
		try {
			singleIntBuffer.clear();
			GLAllocation.generateTextureNames(singleIntBuffer);
			int i = singleIntBuffer.get(0);
			addTexture(readTextureImage(LWJGLMain.loadResourceBytes(var1)), i);
			textureMap.put(var1, Integer.valueOf(i));
			return i;
		} catch (IOException ioexception) {
			throw new RuntimeException("!!");
		}
	}

	public final void addTexture(MinecraftImageData bufferedimage, int i) {
		// if(alpha) {
		// 	GL11.glAlphaFunc(516, 0.1F);
		// }
		bindTexture(i);
		GL11.glTexParameteri(3553 /* GL_TEXTURE_2D */, 10241 /* GL_TEXTURE_MIN_FILTER */, 9728 /* GL_NEAREST */);
		GL11.glTexParameteri(3553 /* GL_TEXTURE_2D */, 10240 /* GL_TEXTURE_MAG_FILTER */, 9728 /* GL_NEAREST */);
		GL11.glTexParameteri(3553 /* GL_TEXTURE_2D */, 10242 /* GL_TEXTURE_WRAP_S */, 10497 /* GL_REPEAT */);
		GL11.glTexParameteri(3553 /* GL_TEXTURE_2D */, 10243 /* GL_TEXTURE_WRAP_T */, 10497 /* GL_REPEAT */);
		int j = bufferedimage.w;
		int k = bufferedimage.h;
		int ai[] = bufferedimage.data;
		byte abyte0[] = new byte[j * k * 4];
		for (int l = 0; l < ai.length; l++) {
			int j1 = ai[l] >> 24 & 0xff;
			int l1 = ai[l] >> 16 & 0xff;
			int j2 = ai[l] >> 8 & 0xff;
			int l2 = ai[l] >> 0 & 0xff;
			abyte0[l * 4 + 0] = (byte) l1;
			abyte0[l * 4 + 1] = (byte) j2;
			abyte0[l * 4 + 2] = (byte) l2;
			abyte0[l * 4 + 3] = (byte) j1;
		}
		textureBuffer.clear();
		textureBuffer.put(abyte0);
		textureBuffer.position(0).limit(abyte0.length);
		GL11.glTexImage2D(3553 /* GL_TEXTURE_2D */, 0, GL11.GL_RGBA /* GL_RGBA */, j, k, 0, GL11.GL_RGBA /* GL_RGBA */, 5121 /* GL_UNSIGNED_BYTE */, textureBuffer);
	}

	public void bindTexture(int i) {
		if (i < 0) {
			return;
		} else {
			GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, i);
			return;
		}
	}

	private MinecraftImageData readTextureImage(byte[] inputstream) throws IOException {
		return LWJGLMain.loadPNG(inputstream);
	}

	public final void registerTextureFX(TextureFX var1) {
		this.textureList.add(var1);
		var1.onTick();
	}
}