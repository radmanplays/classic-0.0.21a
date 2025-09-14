package com.mojang.minecraft.renderer;

import com.mojang.minecraft.phys.AABB;
import com.mojang.util.GLAllocation;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;

import org.lwjgl.opengl.GL11;

public final class Frustum {
	private float[][] m_Frustum = new float[6][4];
	private static Frustum frustum = new Frustum();
	private FloatBuffer _proj = GLAllocation.createFloatBuffer(16);
	private FloatBuffer _modl = GLAllocation.createFloatBuffer(16);
	private FloatBuffer _clip = GLAllocation.createFloatBuffer(16);
	private float[] proj = new float[16];
	private float[] modl = new float[16];
	private float[] clip = new float[16];

	public static Frustum getFrustum() {
		Frustum var0 = frustum;
		var0._proj.clear();
		var0._modl.clear();
		var0._clip.clear();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, var0._proj);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, var0._modl);
		var0._proj.flip().limit(16);
		var0._proj.get(var0.proj);
		var0._modl.flip().limit(16);
		var0._modl.get(var0.modl);
		var0.clip[0] = var0.modl[0] * var0.proj[0] + var0.modl[1] * var0.proj[4] + var0.modl[2] * var0.proj[8] + var0.modl[3] * var0.proj[12];
		var0.clip[1] = var0.modl[0] * var0.proj[1] + var0.modl[1] * var0.proj[5] + var0.modl[2] * var0.proj[9] + var0.modl[3] * var0.proj[13];
		var0.clip[2] = var0.modl[0] * var0.proj[2] + var0.modl[1] * var0.proj[6] + var0.modl[2] * var0.proj[10] + var0.modl[3] * var0.proj[14];
		var0.clip[3] = var0.modl[0] * var0.proj[3] + var0.modl[1] * var0.proj[7] + var0.modl[2] * var0.proj[11] + var0.modl[3] * var0.proj[15];
		var0.clip[4] = var0.modl[4] * var0.proj[0] + var0.modl[5] * var0.proj[4] + var0.modl[6] * var0.proj[8] + var0.modl[7] * var0.proj[12];
		var0.clip[5] = var0.modl[4] * var0.proj[1] + var0.modl[5] * var0.proj[5] + var0.modl[6] * var0.proj[9] + var0.modl[7] * var0.proj[13];
		var0.clip[6] = var0.modl[4] * var0.proj[2] + var0.modl[5] * var0.proj[6] + var0.modl[6] * var0.proj[10] + var0.modl[7] * var0.proj[14];
		var0.clip[7] = var0.modl[4] * var0.proj[3] + var0.modl[5] * var0.proj[7] + var0.modl[6] * var0.proj[11] + var0.modl[7] * var0.proj[15];
		var0.clip[8] = var0.modl[8] * var0.proj[0] + var0.modl[9] * var0.proj[4] + var0.modl[10] * var0.proj[8] + var0.modl[11] * var0.proj[12];
		var0.clip[9] = var0.modl[8] * var0.proj[1] + var0.modl[9] * var0.proj[5] + var0.modl[10] * var0.proj[9] + var0.modl[11] * var0.proj[13];
		var0.clip[10] = var0.modl[8] * var0.proj[2] + var0.modl[9] * var0.proj[6] + var0.modl[10] * var0.proj[10] + var0.modl[11] * var0.proj[14];
		var0.clip[11] = var0.modl[8] * var0.proj[3] + var0.modl[9] * var0.proj[7] + var0.modl[10] * var0.proj[11] + var0.modl[11] * var0.proj[15];
		var0.clip[12] = var0.modl[12] * var0.proj[0] + var0.modl[13] * var0.proj[4] + var0.modl[14] * var0.proj[8] + var0.modl[15] * var0.proj[12];
		var0.clip[13] = var0.modl[12] * var0.proj[1] + var0.modl[13] * var0.proj[5] + var0.modl[14] * var0.proj[9] + var0.modl[15] * var0.proj[13];
		var0.clip[14] = var0.modl[12] * var0.proj[2] + var0.modl[13] * var0.proj[6] + var0.modl[14] * var0.proj[10] + var0.modl[15] * var0.proj[14];
		var0.clip[15] = var0.modl[12] * var0.proj[3] + var0.modl[13] * var0.proj[7] + var0.modl[14] * var0.proj[11] + var0.modl[15] * var0.proj[15];
		var0.m_Frustum[0][0] = var0.clip[3] - var0.clip[0];
		var0.m_Frustum[0][1] = var0.clip[7] - var0.clip[4];
		var0.m_Frustum[0][2] = var0.clip[11] - var0.clip[8];
		var0.m_Frustum[0][3] = var0.clip[15] - var0.clip[12];
		normalizePlane(var0.m_Frustum, 0);
		var0.m_Frustum[1][0] = var0.clip[3] + var0.clip[0];
		var0.m_Frustum[1][1] = var0.clip[7] + var0.clip[4];
		var0.m_Frustum[1][2] = var0.clip[11] + var0.clip[8];
		var0.m_Frustum[1][3] = var0.clip[15] + var0.clip[12];
		normalizePlane(var0.m_Frustum, 1);
		var0.m_Frustum[2][0] = var0.clip[3] + var0.clip[1];
		var0.m_Frustum[2][1] = var0.clip[7] + var0.clip[5];
		var0.m_Frustum[2][2] = var0.clip[11] + var0.clip[9];
		var0.m_Frustum[2][3] = var0.clip[15] + var0.clip[13];
		normalizePlane(var0.m_Frustum, 2);
		var0.m_Frustum[3][0] = var0.clip[3] - var0.clip[1];
		var0.m_Frustum[3][1] = var0.clip[7] - var0.clip[5];
		var0.m_Frustum[3][2] = var0.clip[11] - var0.clip[9];
		var0.m_Frustum[3][3] = var0.clip[15] - var0.clip[13];
		normalizePlane(var0.m_Frustum, 3);
		var0.m_Frustum[4][0] = var0.clip[3] - var0.clip[2];
		var0.m_Frustum[4][1] = var0.clip[7] - var0.clip[6];
		var0.m_Frustum[4][2] = var0.clip[11] - var0.clip[10];
		var0.m_Frustum[4][3] = var0.clip[15] - var0.clip[14];
		normalizePlane(var0.m_Frustum, 4);
		var0.m_Frustum[5][0] = var0.clip[3] + var0.clip[2];
		var0.m_Frustum[5][1] = var0.clip[7] + var0.clip[6];
		var0.m_Frustum[5][2] = var0.clip[11] + var0.clip[10];
		var0.m_Frustum[5][3] = var0.clip[15] + var0.clip[14];
		normalizePlane(var0.m_Frustum, 5);
		return frustum;
	}

	private static void normalizePlane(float[][] var0, int var1) {
		float var2 = (float)Math.sqrt((double)(var0[var1][0] * var0[var1][0] + var0[var1][1] * var0[var1][1] + var0[var1][2] * var0[var1][2]));
		var0[var1][0] /= var2;
		var0[var1][1] /= var2;
		var0[var1][2] /= var2;
		var0[var1][3] /= var2;
	}

	public final boolean cubeInFrustum(float var1, float var2, float var3, float var4, float var5, float var6) {
		for(int var7 = 0; var7 < 6; ++var7) {
			if(this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var3 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var2 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var1 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F && this.m_Frustum[var7][0] * var4 + this.m_Frustum[var7][1] * var5 + this.m_Frustum[var7][2] * var6 + this.m_Frustum[var7][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}

	public final boolean isVisible(AABB var1) {
		return this.cubeInFrustum(var1.x0, var1.y0, var1.z0, var1.x1, var1.y1, var1.z1);
	}
}
