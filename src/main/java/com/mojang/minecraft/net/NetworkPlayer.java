package com.mojang.minecraft.net;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.character.ZombieModel;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.renderer.Textures;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class NetworkPlayer extends Entity {
	public static final long serialVersionUID = 77479605454997290L;
	private ZombieModel zombieModel;
	private float animStep;
	private float animStepO;
	private List moveQueue = new LinkedList();
	private Minecraft minecraft;
	private int xp;
	private int yp;
	private int zp;
	private float yBodyRot = 0.0F;
	private float yBodyRotO = 0.0F;
	private float oRun;
	private float run;
	private transient int skin = -1;
	public transient BufferedImage newTexture = null;
	public String name;
	int tickCount = 0;
	private Textures textures;

	public NetworkPlayer(Minecraft var1, int var2, String var3, int var4, int var5, int var6, float var7, float var8) {
		super(var1.level);
		this.minecraft = var1;
		this.zombieModel = var1.playerModel;
		this.name = var3;
		this.xp = var4;
		this.yp = var5;
		this.zp = var6;
		this.setPos((float)var4 / 32.0F, (float)var5 / 32.0F, (float)var6 / 32.0F);
		this.xRot = var8;
		this.yRot = var7;
		this.heightOffset = 1.62F;
		(new NetworkSkinDownloadThread(this)).start();
	}

	public void tick() {
		super.tick();
		this.animStepO = this.animStep;
		this.yBodyRotO = this.yBodyRot;
		this.yRotO = this.yRot;
		this.xRotO = this.xRot;
		++this.tickCount;
		int var1 = 5;

		do {
			if(this.moveQueue.size() > 0) {
				this.setPos((PlayerMove)this.moveQueue.remove(0));
			}
		} while(var1-- > 0 && this.moveQueue.size() > 10);

		float var7 = this.x - this.xo;
		float var2 = this.z - this.zo;
		float var3 = (float)Math.sqrt((double)(var7 * var7 + var2 * var2));
		float var4 = this.yBodyRot;
		float var5 = 0.0F;
		this.oRun = this.run;
		float var6 = 0.0F;
		if(var3 != 0.0F) {
			var6 = 1.0F;
			var5 = var3 * 3.0F;
			var4 = -((float)Math.atan2((double)var2, (double)var7) * 180.0F / (float)Math.PI + 90.0F);
		}

		this.run += (var6 - this.run) * 0.3F;

		for(var7 = var4 - this.yBodyRot; var7 < -180.0F; var7 += 360.0F) {
		}

		while(var7 >= 180.0F) {
			var7 -= 360.0F;
		}

		this.yBodyRot += var7 * 0.1F;

		for(var7 = this.yRot - this.yBodyRot; var7 < -180.0F; var7 += 360.0F) {
		}

		while(var7 >= 180.0F) {
			var7 -= 360.0F;
		}

		boolean var8 = var7 < -90.0F || var7 >= 90.0F;
		if(var7 < -75.0F) {
			var7 = -75.0F;
		}

		if(var7 >= 75.0F) {
			var7 = 75.0F;
		}

		this.yBodyRot = this.yRot - var7;
		this.yBodyRot += var7 * 0.1F;
		if(var8) {
			var5 = -var5;
		}

		while(this.yRot - this.yRotO < -180.0F) {
			this.yRotO -= 360.0F;
		}

		while(this.yRot - this.yRotO >= 180.0F) {
			this.yRotO += 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO < -180.0F) {
			this.yBodyRotO -= 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO >= 180.0F) {
			this.yBodyRotO += 360.0F;
		}

		while(this.xRot - this.xRotO < -180.0F) {
			this.xRotO -= 360.0F;
		}

		while(this.xRot - this.xRotO >= 180.0F) {
			this.xRotO += 360.0F;
		}

		this.animStep += var5;
	}

	public void render(Textures var1, float var2) {
		this.textures = var1;
		float var3 = this.oRun + (this.run - this.oRun) * var2;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(this.newTexture != null) {
			this.skin = var1.addTexture(this.newTexture);
			this.newTexture = null;
		}

		if(this.skin < 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.getTextureId("/char.png"));
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.skin);
		}

		while(this.yBodyRotO - this.yBodyRot < -180.0F) {
			this.yBodyRotO += 360.0F;
		}

		while(this.yBodyRotO - this.yBodyRot >= 180.0F) {
			this.yBodyRotO -= 360.0F;
		}

		float var9;
		for(var9 = this.yBodyRotO + (this.yBodyRot - this.yBodyRotO) * var2; this.xRotO - this.xRot < -180.0F; this.xRotO += 360.0F) {
		}

		while(this.xRotO - this.xRot >= 180.0F) {
			this.xRotO -= 360.0F;
		}

		while(this.yRotO - this.yRot < -180.0F) {
			this.yRotO += 360.0F;
		}

		while(this.yRotO - this.yRot >= 180.0F) {
			this.yRotO -= 360.0F;
		}

		float var4 = this.yRotO + (this.yRot - this.yRotO) * var2;
		float var5 = this.xRotO + (this.xRot - this.xRotO) * var2;
		var4 = -(var4 - var9);
		GL11.glPushMatrix();
		float var6 = this.animStepO + (this.animStep - this.animStepO) * var2;
		float var7 = this.getBrightness();
		GL11.glColor3f(var7, var7, var7);
		var7 = 1.0F / 16.0F;
		float var8 = (float)(-Math.abs(Math.cos((double)var6 * 0.6662D)) * 5.0D * (double)var3 - 23.0D);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 - this.heightOffset, this.zo + (this.z - this.zo) * var2);
		GL11.glScalef(1.0F, -1.0F, 1.0F);
		GL11.glTranslatef(0.0F, var8 * var7, 0.0F);
		GL11.glRotatef(var9, 0.0F, 1.0F, 0.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glScalef(-1.0F, 1.0F, 1.0F);
		this.zombieModel.render(var6, var3, (float)this.tickCount + var2, var4, var5, var7);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Font var10 = this.minecraft.font;
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 + 0.8F, this.zo + (this.z - this.zo) * var2);
		GL11.glRotatef(-this.minecraft.player.yRot, 0.0F, 1.0F, 0.0F);
		var2 = 0.05F;
		GL11.glScalef(var2, -var2, var2);
		GL11.glTranslatef((float)(-var10.width(this.name)) / 2.0F, 0.0F, 0.0F);
		GL11.glNormal3f(1.0F, -1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		if(this.name.equalsIgnoreCase("Notch")) {
			var10.draw(this.name, 0, 0, 16776960);
		} else {
			var10.draw(this.name, 0, 0, 16777215);
		}

		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslatef(1.0F, 1.0F, -0.05F);
		var10.draw(this.name, 0, 0, 5263440);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void queue(byte var1, byte var2, byte var3, float var4, float var5) {
		float var6 = var4 - this.yRot;

		float var7;
		for(var7 = var5 - this.xRot; var6 >= 180.0F; var6 -= 360.0F) {
		}

		while(var6 < -180.0F) {
			var6 += 360.0F;
		}

		while(var7 >= 180.0F) {
			var7 -= 360.0F;
		}

		while(var7 < -180.0F) {
			var7 += 360.0F;
		}

		var6 = this.yRot + var6 * 0.5F;
		var7 = this.xRot + var7 * 0.5F;
		this.moveQueue.add(new PlayerMove(((float)this.xp + (float)var1 / 2.0F) / 32.0F, ((float)this.yp + (float)var2 / 2.0F) / 32.0F, ((float)this.zp + (float)var3 / 2.0F) / 32.0F, var6, var7));
		this.xp += var1;
		this.yp += var2;
		this.zp += var3;
		this.moveQueue.add(new PlayerMove((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, var4, var5));
	}

	public void teleport(short var1, short var2, short var3, float var4, float var5) {
		float var6 = var4 - this.yRot;

		float var7;
		for(var7 = var5 - this.xRot; var6 >= 180.0F; var6 -= 360.0F) {
		}

		while(var6 < -180.0F) {
			var6 += 360.0F;
		}

		while(var7 >= 180.0F) {
			var7 -= 360.0F;
		}

		while(var7 < -180.0F) {
			var7 += 360.0F;
		}

		var6 = this.yRot + var6 * 0.5F;
		var7 = this.xRot + var7 * 0.5F;
		this.moveQueue.add(new PlayerMove((float)(this.xp + var1) / 64.0F, (float)(this.yp + var2) / 64.0F, (float)(this.zp + var3) / 64.0F, var6, var7));
		this.xp = var1;
		this.yp = var2;
		this.zp = var3;
		this.moveQueue.add(new PlayerMove((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, var4, var5));
	}

	public void queue(byte var1, byte var2, byte var3) {
		this.moveQueue.add(new PlayerMove(((float)this.xp + (float)var1 / 2.0F) / 32.0F, ((float)this.yp + (float)var2 / 2.0F) / 32.0F, ((float)this.zp + (float)var3 / 2.0F) / 32.0F));
		this.xp += var1;
		this.yp += var2;
		this.zp += var3;
		this.moveQueue.add(new PlayerMove((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F));
	}

	public void queue(float var1, float var2) {
		float var3 = var1 - this.yRot;

		float var4;
		for(var4 = var2 - this.xRot; var3 >= 180.0F; var3 -= 360.0F) {
		}

		while(var3 < -180.0F) {
			var3 += 360.0F;
		}

		while(var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		while(var4 < -180.0F) {
			var4 += 360.0F;
		}

		var3 = this.yRot + var3 * 0.5F;
		var4 = this.xRot + var4 * 0.5F;
		this.moveQueue.add(new PlayerMove(var3, var4));
		this.moveQueue.add(new PlayerMove(var1, var2));
	}

	public void clear() {
		if(this.skin >= 0) {
			System.out.println("Releasing texture for " + this.name);
			int var1 = this.skin;
			Textures var2 = this.textures;
			var2.idBuffer.clear();
			var2.idBuffer.put(var1);
			var2.idBuffer.flip();
			GL11.glDeleteTextures(var2.idBuffer);
		}

	}
}
