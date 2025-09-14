package com.mojang.minecraft.character;

import org.lwjgl.opengl.GL11;

public final class Cube {
	private Vertex[] vertices;
	private Polygon[] polygons;
	private int xTexOffs;
	private int yTexOffs;
	private float x;
	private float y;
	private float z;
	public float xRot;
	public float yRot;
	public float zRot;
	private boolean compiled = false;
	private int list = 0;

	public Cube(int var1, int var2) {
		this.xTexOffs = var1;
		this.yTexOffs = var2;
	}

	public final void addBox(float var1, float var2, float var3, int var4, int var5, int var6) {
		this.vertices = new Vertex[8];
		this.polygons = new Polygon[6];
		float var7 = var1 + (float)var4;
		float var8 = var2 + (float)var5;
		float var9 = var3 + (float)var6;
		Vertex var10 = new Vertex(var1, var2, var3, 0.0F, 0.0F);
		Vertex var11 = new Vertex(var7, var2, var3, 0.0F, 8.0F);
		Vertex var12 = new Vertex(var7, var8, var3, 8.0F, 8.0F);
		Vertex var16 = new Vertex(var1, var8, var3, 8.0F, 0.0F);
		Vertex var13 = new Vertex(var1, var2, var9, 0.0F, 0.0F);
		Vertex var15 = new Vertex(var7, var2, var9, 0.0F, 8.0F);
		Vertex var17 = new Vertex(var7, var8, var9, 8.0F, 8.0F);
		Vertex var14 = new Vertex(var1, var8, var9, 8.0F, 0.0F);
		this.vertices[0] = var10;
		this.vertices[1] = var11;
		this.vertices[2] = var12;
		this.vertices[3] = var16;
		this.vertices[4] = var13;
		this.vertices[5] = var15;
		this.vertices[6] = var17;
		this.vertices[7] = var14;
		this.polygons[0] = new Polygon(new Vertex[]{var15, var11, var12, var17}, this.xTexOffs + var6 + var4, this.yTexOffs + var6, this.xTexOffs + var6 + var4 + var6, this.yTexOffs + var6 + var5);
		this.polygons[1] = new Polygon(new Vertex[]{var10, var13, var14, var16}, this.xTexOffs, this.yTexOffs + var6, this.xTexOffs + var6, this.yTexOffs + var6 + var5);
		this.polygons[2] = new Polygon(new Vertex[]{var15, var13, var10, var11}, this.xTexOffs + var6, this.yTexOffs, this.xTexOffs + var6 + var4, this.yTexOffs + var6);
		this.polygons[3] = new Polygon(new Vertex[]{var12, var16, var14, var17}, this.xTexOffs + var6 + var4, this.yTexOffs, this.xTexOffs + var6 + var4 + var4, this.yTexOffs + var6);
		this.polygons[4] = new Polygon(new Vertex[]{var11, var10, var16, var12}, this.xTexOffs + var6, this.yTexOffs + var6, this.xTexOffs + var6 + var4, this.yTexOffs + var6 + var5);
		this.polygons[5] = new Polygon(new Vertex[]{var13, var15, var17, var14}, this.xTexOffs + var6 + var4 + var6, this.yTexOffs + var6, this.xTexOffs + var6 + var4 + var6 + var4, this.yTexOffs + var6 + var5);
	}

	public final void setPos(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = 0.0F;
	}

	public final void render(float var1) {
		if(!this.compiled) {
			float var3 = var1;
			Cube var2 = this;
			this.list = GL11.glGenLists(1);
			GL11.glNewList(this.list, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);

			for(int var4 = 0; var4 < var2.polygons.length; ++var4) {
				Polygon var10000 = var2.polygons[var4];
				float var6 = var3;
				Polygon var5 = var10000;
				Vec3 var7 = var5.vertices[1].pos.subtract(var5.vertices[0].pos).normalize();
				Vec3 var8 = var5.vertices[1].pos.subtract(var5.vertices[2].pos).normalize();
				var7 = (new Vec3(var7.y * var8.z - var7.z * var8.y, var7.z * var8.x - var7.x * var8.z, var7.x * var8.y - var7.y * var8.x)).normalize();
				GL11.glNormal3f(var7.x, var7.y, var7.z);

				for(int var10 = 0; var10 < 4; ++var10) {
					Vertex var11 = var5.vertices[var10];
					GL11.glTexCoord2f(var11.u / 64.0F, var11.v / 32.0F);
					GL11.glVertex3f(var11.pos.x * var6, var11.pos.y * var6, var11.pos.z * var6);
				}
			}

			GL11.glEnd();
			GL11.glEndList();
			var2.compiled = true;
		}

		float var9 = 57.29578F;
		GL11.glPushMatrix();
		GL11.glTranslatef(this.x * var1, this.y * var1, this.z * var1);
		GL11.glRotatef(this.zRot * var9, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(this.yRot * var9, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.xRot * var9, 1.0F, 0.0F, 0.0F);
		GL11.glCallList(this.list);
		GL11.glPopMatrix();
	}
}
