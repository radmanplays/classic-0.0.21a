package com.mojang.minecraft.renderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.lax1dude.eaglercraft.adapter.RealOpenGLEnums;

public final class Tesselator {
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(524288);
	private float[] array = new float[524288];
	private int vertices = 0;
	private float u;
	private float v;
	private float r;
	private float g;
	private float b;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private int len = 3;
	private int p = 0;
	private boolean noColor = false;
	public static Tesselator instance = new Tesselator();

	public final void end() {
		if(this.vertices > 0) {
			this.buffer.clear();
			this.buffer.put(this.array, 0, this.p);
			this.buffer.flip();
//			if(this.hasTexture && this.hasColor) {
//				GL11.glInterleavedArrays(RealOpenGLEnums.GL_T2F_C3F_V3F, 0, (FloatBuffer)this.buffer);
//			} else if(this.hasTexture) {
//				GL11.glInterleavedArrays(RealOpenGLEnums.GL_T2F_V3F, 0, (FloatBuffer)this.buffer);
//			} else if(this.hasColor) {
//				GL11.glInterleavedArrays(RealOpenGLEnums.GL_C3F_V3F, 0, (FloatBuffer)this.buffer);
//			} else {
//				GL11.glInterleavedArrays(RealOpenGLEnums.GL_V3F, 0, (FloatBuffer)this.buffer);
//			}

			GL11.glEnableClientState(RealOpenGLEnums.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glEnableClientState(RealOpenGLEnums.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			}

			GL11.glDrawArrays(GL11.GL_QUADS, GL11.GL_POINTS, this.vertices);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			}
		}

		this.clear();
	}

	private void clear() {
		this.vertices = 0;
		this.buffer.clear();
		this.p = 0;
	}

	public final void begin() {
		this.clear();
		this.hasColor = false;
		this.hasTexture = false;
		this.noColor = false;
	}

	public final void color(float var1, float var2, float var3) {
		if(!this.noColor) {
			if(!this.hasColor) {
				this.len += 3;
			}

			this.hasColor = true;
			this.r = var1;
			this.g = var2;
			this.b = var3;
		}
	}

	public final void color(int var1, int var2, int var3) {
		byte var10001 = (byte)var1;
		byte var10002 = (byte)var2;
		byte var6 = (byte)var3;
		byte var5 = var10002;
		byte var4 = var10001;
		if(!this.noColor) {
			if(!this.hasColor) {
				this.len += 3;
			}

			this.hasColor = true;
			this.r = (float)(var4 & 255) / 255.0F;
			this.g = (float)(var5 & 255) / 255.0F;
			this.b = (float)(var6 & 255) / 255.0F;
		}

	}

	public final void vertexUV(float var1, float var2, float var3, float var4, float var5) {
		if(!this.hasTexture) {
			this.len += 2;
		}

		this.hasTexture = true;
		this.u = var4;
		this.v = var5;
		this.vertex(var1, var2, var3);
	}

	public final void vertex(float var1, float var2, float var3) {
		if(this.hasTexture) {
			this.array[this.p++] = this.u;
			this.array[this.p++] = this.v;
		}

		if(this.hasColor) {
			this.array[this.p++] = this.r;
			this.array[this.p++] = this.g;
			this.array[this.p++] = this.b;
		}

		this.array[this.p++] = var1;
		this.array[this.p++] = var2;
		this.array[this.p++] = var3;
		++this.vertices;
		if(this.vertices % 4 == 0 && this.p >= 524288 - (this.len << 2)) {
			this.end();
		}

	}

	public final void color(int var1) {
		int var2 = var1 >> 16 & 255;
		int var3 = var1 >> 8 & 255;
		var1 &= 255;
		this.color(var2, var3, var1);
	}

	public final void noColor() {
		this.noColor = true;
	}
}
