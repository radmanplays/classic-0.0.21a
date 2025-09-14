package com.mojang.minecraft.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mojang.minecraft.renderer.texture.TextureFX;
import com.mojang.util.GLAllocation;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.opengl.ImageData;
import org.lwjgl.opengl.GL11;

import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;

public class Textures {
	private HashMap idMap = new HashMap();
	public IntBuffer idBuffer = GLAllocation.createIntBuffer(1);
	public ByteBuffer textureBuffer = GLAllocation.createByteBuffer(262144);
	public List textureList = new ArrayList();
	
	public final int getTextureId(String var1) {
			if(this.idMap.containsKey(var1)) {
				return ((Integer)this.idMap.get(var1)).intValue();
			} else {
				int var2 = this.addTexture(ImageData.loadImageFile(EagRuntime.getResourceStream(var1)));
				this.idMap.put(var1, Integer.valueOf(var2));
				return var2;
			}
	}

	public final int addTexture(ImageData img) {
			this.idBuffer.clear();
            GL11.glGenTextures(this.idBuffer);
            int id = this.idBuffer.get(0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            int w = img.width;
            int h = img.height;
            int[] rawPixels = new int[w * h];
            byte[] var6 = new byte[w * h << 2];
            img.getRGB(0, 0, w, h, rawPixels, 0, w);

            for(int i = 0; i < rawPixels.length; ++i) {
                int a = rawPixels[i] >>> 24;
                int r = rawPixels[i] >> 16 & 255;
                int g = rawPixels[i] >> 8 & 255;
                int b = rawPixels[i] & 255;
				var6[i << 2] = (byte)b;
				var6[(i << 2) + 1] = (byte)g;
				var6[(i << 2) + 2] = (byte)r;
				var6[(i << 2) + 3] = (byte)a;
            }

    		this.textureBuffer.clear();
    		this.textureBuffer.put(var6);
    		this.textureBuffer.position(0).limit(var6.length);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.textureBuffer);
            return id;
    }
	
	public final void registerTextureFX(TextureFX var1) {
		this.textureList.add(var1);
		var1.onTick();
	}
}