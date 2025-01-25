package com.mojang.minecraft.renderer;

import java.util.ArrayList;

public class TextureLocation {

	private String path;
	private int glObject;

	public TextureLocation(String path) {
		this.path = path;
		this.glObject = -1;
		locations.add(this);
	}

	public static void freeTextures() {
		for (TextureLocation l : locations) {
			l.glObject = -1;
		}
	}

	public int getTexturePointer() {
		Textures t = new Textures();
		if (glObject == -1) {
			glObject = t.getTextureId(path);
			if (glObject == -1) {
				System.err.println("could not load: " + path);
			}
		}
		return glObject;
	}
	
	public int bindTexture() {
		Textures t = new Textures();
		int i = getTexturePointer();
		if(i != -1) {
			t.bindTexture(i);
		}
		return i;
	}

	private static final ArrayList<TextureLocation> locations = new ArrayList();
}