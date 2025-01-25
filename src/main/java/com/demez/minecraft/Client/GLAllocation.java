package com.demez.minecraft.Client;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class GLAllocation {

	public GLAllocation() {
	}

	public static synchronized int generateDisplayLists(int i) {
		int j = GL11.glGenLists(i);
		displayLists.add(Integer.valueOf(j));
		displayLists.add(Integer.valueOf(i));
		return j;
	}

	public static synchronized void generateTextureNames(IntBuffer intbuffer) {
		
		for (int i = intbuffer.position(); i < intbuffer.limit(); i++) {
			int tx = GL11.glGenTextures();
			intbuffer.put(i, tx);
			textureNames.add(Integer.valueOf(tx));
		}

	}

	public static synchronized void deleteTexturesAndDisplayLists() {
		for (int i = 0; i < displayLists.size(); i += 2) {
			GL11.glDeleteLists(((Integer) displayLists.get(i)).intValue(),
					((Integer) displayLists.get(i + 1)).intValue());
		}
		
		for (int j = 0; j < textureNames.size(); j++) {
			GL11.glDeleteTextures(((Integer) textureNames.get(j)).intValue());
		}
		
		displayLists.clear();
		textureNames.clear();
	}
	
	public static ByteBuffer createDirectByteBuffer(int par0) {
		return ByteBuffer.wrap(new byte[par0]).order(ByteOrder.nativeOrder());
	}
	
	public static IntBuffer createDirectIntBuffer(int par0) {
		return IntBuffer.wrap(new int[par0]);
	}
	
	public static FloatBuffer createDirectFloatBuffer(int par0) {
		return FloatBuffer.wrap(new float[par0]);
	}

	private static List displayLists = new ArrayList();
	private static List textureNames = new ArrayList();

}