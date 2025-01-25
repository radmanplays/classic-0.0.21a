package com.mojang.minecraft.gui;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import com.demez.minecraft.Client.FontAllowedCharacters;
import com.mojang.minecraft.renderer.TextureLocation;

import net.PeytonPlayz585.opengl.LWJGLMain;
import net.PeytonPlayz585.opengl.MinecraftImageData;

public final class Font {
	private int charWidths[];
	public String fontTextureName;
	private int fontDisplayLists;
	private IntBuffer buffer;
	
	public static final char formatChar = '\247';


	public Font(String var1) {
		charWidths = new int[256];
		fontTextureName = null;
		MinecraftImageData bufferedimage = LWJGLMain.loadPNG(LWJGLMain.loadResourceBytes(var1));
		int i = bufferedimage.w;
		int j = bufferedimage.h;
		int ai[] = bufferedimage.data;
		for (int k = 0; k < 256; k++) {
			int l = k % 16;
			int k1 = k / 16;
			int j2 = 7;
			do {
				if (j2 < 0) {
					break;
				}
				int i3 = l * 8 + j2;
				boolean flag = true;
				for (int l3 = 0; l3 < 8 && flag; l3++) {
					int i4 = (k1 * 8 + l3) * i;
					int k4 = ai[i3 + i4] & 0xff;
					if (k4 > 0) {
						flag = false;
					}
				}

				if (!flag) {
					break;
				}
				j2--;
			} while (true);
			if (k == 32) {
				j2 = 2;
			}
			charWidths[k] = j2 + 2;
		}

		

		for (int j1 = 0; j1 < 32; j1++) {
			int i2 = (j1 >> 3 & 1) * 85;
			int l2 = (j1 >> 2 & 1) * 170 + i2;
			int j3 = (j1 >> 1 & 1) * 170 + i2;
			int k3 = (j1 >> 0 & 1) * 170 + i2;
			if (j1 == 6) {
				l2 += 85;
			}
			boolean flag1 = j1 >= 16;
			if (flag1) {
				l2 /= 4;
				j3 /= 4;
				k3 /= 4;
			}
			GL11.glNewList(fontDisplayLists + 256 + j1, 4864 /* GL_COMPILE */);
			GL11.glColor3f((float) l2 / 255F, (float) j3 / 255F, (float) k3 / 255F);
			GL11.glEndList();
		}
	}

	public final void drawShadow(String var1, int var2, int var3, int var4) {
		this.draw(var1, var2 + 1, var3 + 1, var4, true);
		this.draw(var1, var2, var3, var4);
	}

	public final void draw(String var1, int var2, int var3, int var4) {
		this.draw(var1, var2, var3, var4, false);
	}

	private void draw(String s, int i, int j, int k, boolean flag) {
		if (s == null) {
			return;
		}
		if (flag) {
			int l = k & 0xff000000;
			k = (k & 0xfcfcfc) >> 2;
			k += l;
		}
		new TextureLocation(fontTextureName).bindTexture();
		float f = (float) (k >> 16 & 0xff) / 255F;
		float f1 = (float) (k >> 8 & 0xff) / 255F;
		float f2 = (float) (k & 0xff) / 255F;
		float f3 = (float) (k >> 24 & 0xff) / 255F;
		if (f3 == 0.0F) {
			f3 = 1.0F;
		}
		GL11.glColor4f(f, f1, f2, f3);
		GL11.glPushMatrix();
		GL11.glTranslatef(i, j, 0.0F);
		for (int i1 = 0; i1 < s.length(); i1++) {
			for (; s.length() > i1 + 1 && s.charAt(i1) == '\247'; i1 += 2) {
				int j1 = "0123456789abcdef".indexOf(s.toLowerCase().charAt(i1 + 1));
				if (j1 < 0 || j1 > 15) {
					j1 = 15;
				}
				continue;
			}

			if (i1 < s.length()) {
				int k1 = FontAllowedCharacters.isAllowed(s.charAt(i1));
				if (k1 >= 0) {
					GL11.glCallList(fontDisplayLists + k1 + 32);
					GL11.glTranslatef(charWidths[k1 + 32], 0.0F, 0.0F);
				}
			}
		}
		
		GL11.glPopMatrix();
	}

	public final int width(String s) {
		if (s == null) {
			return 0;
		}
		int i = 0;
		for (int j = 0; j < s.length(); j++) {
			if (s.charAt(j) == '\247') {
				j++;
				continue;
			}
			int k = FontAllowedCharacters.isAllowed(s.charAt(j));
			if (k >= 0) {
				i += charWidths[k + 32];
			}
		}

		return i;
	}
	
	
}