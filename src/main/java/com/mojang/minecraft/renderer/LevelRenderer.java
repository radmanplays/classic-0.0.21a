package com.mojang.minecraft.renderer;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import net.lax1dude.eaglercraft.adapter.RealOpenGLEnums;

public final class LevelRenderer {
	public Level level;
	public Textures textures;
	public int surroundLists;
	public int drawDistance = 0;
	public IntBuffer dummyBuffer = BufferUtils.createIntBuffer(65536);
	public Set dirtyChunks = new HashSet();
	private Chunk[] chunks;
	public Chunk[] sortedChunks;
	private int xChunks;
	private int yChunks;
	private int zChunks;
	private int chunkRenderLists;
	public int cloudTickCounter = 0;
	private float lX = -9999.0F;
	private float lY = -9999.0F;
	private float lZ = -9999.0F;

	public LevelRenderer(Textures var1) {
		this.textures = var1;
		this.surroundLists = GL11.glGenLists(2);
		this.chunkRenderLists = GL11.glGenLists(4096 << 6 << 1);
	}

	public final void compileSurroundingGround() {
		int var1;
		if(this.sortedChunks != null) {
			for(var1 = 0; var1 < this.sortedChunks.length; ++var1) {
				this.sortedChunks[var1].clear();
			}
		}

		this.xChunks = this.level.width / 16;
		this.yChunks = this.level.depth / 16;
		this.zChunks = this.level.height / 16;
		this.sortedChunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		var1 = 0;

		int var4;
		for(int var2 = 0; var2 < this.xChunks; ++var2) {
			for(int var3 = 0; var3 < this.yChunks; ++var3) {
				for(var4 = 0; var4 < this.zChunks; ++var4) {
					this.sortedChunks[(var4 * this.yChunks + var3) * this.xChunks + var2] = new Chunk(this.level, var2 << 4, var3 << 4, var4 << 4, 16, this.chunkRenderLists + var1);
					this.chunks[(var4 * this.yChunks + var3) * this.xChunks + var2] = this.sortedChunks[(var4 * this.yChunks + var3) * this.xChunks + var2];
					var1 += 2;
				}
			}
		}

		this.dirtyChunks.clear();
		GL11.glNewList(this.surroundLists, RealOpenGLEnums.GL_COMPILE);
		LevelRenderer var9 = this;
		GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
		GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, this.textures.getTextureId("/rock.png"));
		float var10 = 0.5F;
		GL11.glColor4f(var10, var10, var10, 1.0F);
		Tesselator var11 = Tesselator.instance;
		float var12 = this.level.getGroundLevel();
		int var5 = 128;
		if(128 > this.level.width) {
			var5 = this.level.width;
		}

		if(var5 > this.level.height) {
			var5 = this.level.height;
		}

		int var6 = 2048 / var5;
		var11.begin();

		int var7;
		for(var7 = -var5 * var6; var7 < var9.level.width + var5 * var6; var7 += var5) {
			for(int var8 = -var5 * var6; var8 < var9.level.height + var5 * var6; var8 += var5) {
				var10 = var12;
				if(var7 >= 0 && var8 >= 0 && var7 < var9.level.width && var8 < var9.level.height) {
					var10 = 0.0F;
				}

				var11.vertexUV((float)var7, var10, (float)(var8 + var5), 0.0F, (float)var5);
				var11.vertexUV((float)(var7 + var5), var10, (float)(var8 + var5), (float)var5, (float)var5);
				var11.vertexUV((float)(var7 + var5), var10, (float)var8, (float)var5, 0.0F);
				var11.vertexUV((float)var7, var10, (float)var8, 0.0F, 0.0F);
			}
		}

		var11.end();
		GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, var9.textures.getTextureId("/rock.png"));
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		var11.begin();

		for(var7 = 0; var7 < var9.level.width; var7 += var5) {
			var11.vertexUV((float)var7, 0.0F, 0.0F, 0.0F, 0.0F);
			var11.vertexUV((float)(var7 + var5), 0.0F, 0.0F, (float)var5, 0.0F);
			var11.vertexUV((float)(var7 + var5), var12, 0.0F, (float)var5, var12);
			var11.vertexUV((float)var7, var12, 0.0F, 0.0F, var12);
			var11.vertexUV((float)var7, var12, (float)var9.level.height, 0.0F, var12);
			var11.vertexUV((float)(var7 + var5), var12, (float)var9.level.height, (float)var5, var12);
			var11.vertexUV((float)(var7 + var5), 0.0F, (float)var9.level.height, (float)var5, 0.0F);
			var11.vertexUV((float)var7, 0.0F, (float)var9.level.height, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(var7 = 0; var7 < var9.level.height; var7 += var5) {
			var11.vertexUV(0.0F, var12, (float)var7, 0.0F, 0.0F);
			var11.vertexUV(0.0F, var12, (float)(var7 + var5), (float)var5, 0.0F);
			var11.vertexUV(0.0F, 0.0F, (float)(var7 + var5), (float)var5, var12);
			var11.vertexUV(0.0F, 0.0F, (float)var7, 0.0F, var12);
			var11.vertexUV((float)var9.level.width, 0.0F, (float)var7, 0.0F, var12);
			var11.vertexUV((float)var9.level.width, 0.0F, (float)(var7 + var5), (float)var5, var12);
			var11.vertexUV((float)var9.level.width, var12, (float)(var7 + var5), (float)var5, 0.0F);
			var11.vertexUV((float)var9.level.width, var12, (float)var7, 0.0F, 0.0F);
		}

		var11.end();
		GL11.glDisable(RealOpenGLEnums.GL_BLEND);
		GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		GL11.glEndList();
		GL11.glNewList(this.surroundLists + 1, RealOpenGLEnums.GL_COMPILE);
		var9 = this;
		GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, this.textures.getTextureId("/water.png"));
		var10 = this.level.getWaterLevel();
		GL11.glEnable(RealOpenGLEnums.GL_BLEND);
		GL11.glBlendFunc(RealOpenGLEnums.GL_SRC_ALPHA, RealOpenGLEnums.GL_ONE_MINUS_SRC_ALPHA);
		var11 = Tesselator.instance;
		var4 = 128;
		if(128 > this.level.width) {
			var4 = this.level.width;
		}

		if(var4 > this.level.height) {
			var4 = this.level.height;
		}

		var5 = 2048 / var4;
		var11.begin();

		for(var6 = -var4 * var5; var6 < var9.level.width + var4 * var5; var6 += var4) {
			for(var7 = -var4 * var5; var7 < var9.level.height + var4 * var5; var7 += var4) {
				float var13 = var10 - 0.1F;
				if(var6 < 0 || var7 < 0 || var6 >= var9.level.width || var7 >= var9.level.height) {
					var11.vertexUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
					var11.vertexUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
					var11.vertexUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
					var11.vertexUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.vertexUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.vertexUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
					var11.vertexUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
					var11.vertexUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
				}
			}
		}

		var11.end();
		GL11.glDisable(RealOpenGLEnums.GL_BLEND);
		GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		GL11.glEndList();
		this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
	}

	public final void renderEntities(Frustum var1, float var2) {
		for(int var3 = 0; var3 < this.level.entities.size(); ++var3) {
			Entity var4 = (Entity)this.level.entities.get(var3);
			if(var1.isVisible(var4.bb)) {
				((Entity)this.level.entities.get(var3)).render(this.textures, var2);
			}
		}

	}

	public final int render(Player var1, int var2) {
		float var3 = var1.x - this.lX;
		float var4 = var1.y - this.lY;
		float var5 = var1.z - this.lZ;
		if(var3 * var3 + var4 * var4 + var5 * var5 > 64.0F) {
			this.lX = var1.x;
			this.lY = var1.y;
			this.lZ = var1.z;
			Arrays.sort(this.chunks, new DistanceSorter(var1));
		}

		this.dummyBuffer.clear();

		for(int var6 = 0; var6 < this.chunks.length; ++var6) {
			this.chunks[var6].render(this.dummyBuffer, var2);
		}

		this.dummyBuffer.flip();
		if(this.dummyBuffer.remaining() > 0) {
			GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, this.textures.getTextureId("/terrain.png"));
			GL11.glCallLists(this.dummyBuffer);
			GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		}

		return this.dummyBuffer.remaining();
	}

	public final void renderClouds(float var1) {
		GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
		GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, this.textures.getTextureId("/clouds.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Tesselator var2 = Tesselator.instance;
		float var3 = 0.0F;
		float var4 = 0.5F / 1024.0F;
		var3 = (float)(this.level.depth + 2);
		var1 = ((float)this.cloudTickCounter + var1) * var4 * 0.03F;
		float var5 = 0.0F;
		var2.begin();

		int var8;
		for(var8 = -2048; var8 < this.level.width + 2048; var8 += 512) {
			for(int var6 = -2048; var6 < this.level.height + 2048; var6 += 512) {
				var2.vertexUV((float)var8, var3, (float)(var6 + 512), (float)var8 * var4 + var1, (float)(var6 + 512) * var4);
				var2.vertexUV((float)(var8 + 512), var3, (float)(var6 + 512), (float)(var8 + 512) * var4 + var1, (float)(var6 + 512) * var4);
				var2.vertexUV((float)(var8 + 512), var3, (float)var6, (float)(var8 + 512) * var4 + var1, (float)var6 * var4);
				var2.vertexUV((float)var8, var3, (float)var6, (float)var8 * var4 + var1, (float)var6 * var4);
				var2.vertexUV((float)var8, var3, (float)var6, (float)var8 * var4 + var1, (float)var6 * var4);
				var2.vertexUV((float)(var8 + 512), var3, (float)var6, (float)(var8 + 512) * var4 + var1, (float)var6 * var4);
				var2.vertexUV((float)(var8 + 512), var3, (float)(var6 + 512), (float)(var8 + 512) * var4 + var1, (float)(var6 + 512) * var4);
				var2.vertexUV((float)var8, var3, (float)(var6 + 512), (float)var8 * var4 + var1, (float)(var6 + 512) * var4);
			}
		}

		var2.end();
		GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		var2.begin();
		var2.color(0.5F, 0.8F, 1.0F);
		var3 = (float)(this.level.depth + 10);

		for(int var7 = -2048; var7 < this.level.width + 2048; var7 += 512) {
			for(var8 = -2048; var8 < this.level.height + 2048; var8 += 512) {
				var2.vertex((float)var7, var3, (float)var8);
				var2.vertex((float)(var7 + 512), var3, (float)var8);
				var2.vertex((float)(var7 + 512), var3, (float)(var8 + 512));
				var2.vertex((float)var7, var3, (float)(var8 + 512));
			}
		}

		var2.end();
	}

	public final void render(int var1, int var2, int var3) {
		int var6 = this.level.getTile(var1, var2, var3);
		if(var6 != 0 && Tile.tiles[var6].isSolid()) {
			GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
			GL11.glDepthFunc(RealOpenGLEnums.GL_LESS);
			Tesselator var4 = Tesselator.instance;
			var4.begin();

			int var5;
			for(var5 = 0; var5 < 6; ++var5) {
				Tile.tiles[var6].renderFace(var4, var1, var2, var3, var5);
			}

			var4.end();
			GL11.glCullFace(RealOpenGLEnums.GL_FRONT);
			var4.begin();

			for(var5 = 0; var5 < 6; ++var5) {
				Tile.tiles[var6].renderFace(var4, var1, var2, var3, var5);
			}

			var4.end();
			GL11.glCullFace(RealOpenGLEnums.GL_BACK);
			GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
			GL11.glDepthFunc(RealOpenGLEnums.GL_LEQUAL);
		}
	}

	public final void renderHit(Player var1, HitResult var2, int var3, int var4) {
		Tesselator var5 = Tesselator.instance;
		GL11.glEnable(RealOpenGLEnums.GL_BLEND);
		GL11.glEnable(RealOpenGLEnums.GL_ALPHA_TEST);
		GL11.glBlendFunc(RealOpenGLEnums.GL_SRC_ALPHA, RealOpenGLEnums.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
		if(var3 == 0) {
			var5.begin();

			for(var3 = 0; var3 < 6; ++var3) {
				Tile.renderFaceNoTexture(var1, var5, var2.x, var2.y, var2.z, var3);
			}

			var5.end();
		} else {
			GL11.glBlendFunc(RealOpenGLEnums.GL_SRC_ALPHA, RealOpenGLEnums.GL_ONE_MINUS_SRC_ALPHA);
			float var8 = (float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.8F;
			GL11.glColor4f(var8, var8, var8, (float)Math.sin((double)System.currentTimeMillis() / 200.0D) * 0.2F + 0.5F);
			GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			int var7 = this.textures.getTextureId("/terrain.png");
			GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, var7);
			var7 = var2.x;
			var3 = var2.y;
			int var6 = var2.z;
			if(var2.f == 0) {
				--var3;
			}

			if(var2.f == 1) {
				++var3;
			}

			if(var2.f == 2) {
				--var6;
			}

			if(var2.f == 3) {
				++var6;
			}

			if(var2.f == 4) {
				--var7;
			}

			if(var2.f == 5) {
				++var7;
			}

			var5.begin();
			var5.noColor();
			Tile.tiles[var4].render(var5, this.level, 0, var7, var3, var6);
			Tile.tiles[var4].render(var5, this.level, 1, var7, var3, var6);
			var5.end();
			GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		}

		GL11.glDisable(RealOpenGLEnums.GL_BLEND);
		GL11.glDisable(RealOpenGLEnums.GL_ALPHA_TEST);
	}

	public static void renderHitOutline(HitResult var0, int var1) {
		GL11.glEnable(RealOpenGLEnums.GL_BLEND);
		GL11.glBlendFunc(RealOpenGLEnums.GL_SRC_ALPHA, RealOpenGLEnums.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		float var2 = (float)var0.x;
		float var3 = (float)var0.y;
		float var4 = (float)var0.z;
		if(var1 == 1) {
			if(var0.f == 0) {
				--var3;
			}

			if(var0.f == 1) {
				++var3;
			}

			if(var0.f == 2) {
				--var4;
			}

			if(var0.f == 3) {
				++var4;
			}

			if(var0.f == 4) {
				--var2;
			}

			if(var0.f == 5) {
				++var2;
			}
		}

		GL11.glBegin(RealOpenGLEnums.GL_LINE_STRIP);
		GL11.glVertex3f(var2, var3, var4);
		GL11.glVertex3f(var2 + 1.0F, var3, var4);
		GL11.glVertex3f(var2 + 1.0F, var3, var4 + 1.0F);
		GL11.glVertex3f(var2, var3, var4 + 1.0F);
		GL11.glVertex3f(var2, var3, var4);
		GL11.glEnd();
		GL11.glBegin(RealOpenGLEnums.GL_LINE_STRIP);
		GL11.glVertex3f(var2, var3 + 1.0F, var4);
		GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4);
		GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4 + 1.0F);
		GL11.glVertex3f(var2, var3 + 1.0F, var4 + 1.0F);
		GL11.glVertex3f(var2, var3 + 1.0F, var4);
		GL11.glEnd();
		GL11.glBegin(RealOpenGLEnums.GL_LINES);
		GL11.glVertex3f(var2, var3, var4);
		GL11.glVertex3f(var2, var3 + 1.0F, var4);
		GL11.glVertex3f(var2 + 1.0F, var3, var4);
		GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4);
		GL11.glVertex3f(var2 + 1.0F, var3, var4 + 1.0F);
		GL11.glVertex3f(var2 + 1.0F, var3 + 1.0F, var4 + 1.0F);
		GL11.glVertex3f(var2, var3, var4 + 1.0F);
		GL11.glVertex3f(var2, var3 + 1.0F, var4 + 1.0F);
		GL11.glEnd();
		GL11.glDisable(RealOpenGLEnums.GL_BLEND);
	}

	public final void setDirty(int var1, int var2, int var3, int var4, int var5, int var6) {
		var1 /= 16;
		var2 /= 16;
		var3 /= 16;
		var4 /= 16;
		var5 /= 16;
		var6 /= 16;
		if(var1 < 0) {
			var1 = 0;
		}

		if(var2 < 0) {
			var2 = 0;
		}

		if(var3 < 0) {
			var3 = 0;
		}

		if(var4 > this.xChunks - 1) {
			var4 = this.xChunks - 1;
		}

		if(var5 > this.yChunks - 1) {
			var5 = this.yChunks - 1;
		}

		if(var6 > this.zChunks - 1) {
			var6 = this.zChunks - 1;
		}

		for(var1 = var1; var1 <= var4; ++var1) {
			for(int var7 = var2; var7 <= var5; ++var7) {
				for(int var8 = var3; var8 <= var6; ++var8) {
					this.dirtyChunks.add(this.sortedChunks[(var8 * this.yChunks + var7) * this.xChunks + var1]);
				}
			}
		}

	}
}
