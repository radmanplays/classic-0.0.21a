package com.mojang.minecraft.gui;

import com.mojang.minecraft.ChatLine;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class InGameHud {
	public List messages = new ArrayList();
	private Minecraft minecraft;
	private int scaledWidth;
	private int scaledHeight;

	public InGameHud(Minecraft var1, int var2, int var3) {
		this.minecraft = var1;
		this.scaledWidth = var2 * 240 / var3;
		this.scaledHeight = var3 * 240 / var3;
	}

	public final void render() {
		Font var1 = this.minecraft.font;
		this.minecraft.initGui();
		Textures var2 = this.minecraft.textures;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.getTextureId("/gui.png"));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		Tesselator var3 = Tesselator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		Inventory var4 = this.minecraft.player.inventory;
		blit(this.scaledWidth / 2 - 91, this.scaledHeight - 22, 0, 0, 182, 22);
		blit(this.scaledWidth / 2 - 91 - 1 + var4.selectedSlot * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glDisable(GL11.GL_BLEND);

		int var5;
		int var7;
		for(var5 = 0; var5 < var4.slots.length; ++var5) {
			int var6 = var4.slots[var5];
			if(var6 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(this.scaledWidth / 2 - 90 + var5 * 20), (float)(this.scaledHeight - 16), -50.0F);
				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 0.0F);
				GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
				GL11.glScalef(-1.0F, -1.0F, -1.0F);
				var7 = var2.getTextureId("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				var3.begin();
				Tile.tiles[var6].render(var3, this.minecraft.level, 0, -2, 0, 0);
				var3.end();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
			}
		}

		var1.drawShadow("0.0.21a", 2, 2, 16777215);
		var1.drawShadow(this.minecraft.fpsString, 2, 12, 16777215);
		byte var17 = 10;
		boolean var18 = false;
		if(this.minecraft.screen instanceof ChatScreen) {
			var17 = 20;
			var18 = true;
		}

		for(var7 = 0; var7 < this.messages.size() && var7 < var17; ++var7) {
			if(((ChatLine)this.messages.get(var7)).counter < 200 || var18) {
				var1.drawShadow(((ChatLine)this.messages.get(var7)).message, 2, this.scaledHeight - 8 - (var7 << 3) - 16, 16777215);
			}
		}

		var7 = this.scaledWidth / 2;
		int var11 = this.scaledHeight / 2;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var3.begin();
		var3.vertex((float)(var7 + 1), (float)(var11 - 4), 0.0F);
		var3.vertex((float)var7, (float)(var11 - 4), 0.0F);
		var3.vertex((float)var7, (float)(var11 + 5), 0.0F);
		var3.vertex((float)(var7 + 1), (float)(var11 + 5), 0.0F);
		var3.vertex((float)(var7 + 5), (float)var11, 0.0F);
		var3.vertex((float)(var7 - 4), (float)var11, 0.0F);
		var3.vertex((float)(var7 - 4), (float)(var11 + 1), 0.0F);
		var3.vertex((float)(var7 + 5), (float)(var11 + 1), 0.0F);
		var3.end();
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && this.minecraft.connectionManager != null && this.minecraft.connectionManager.isConnected()) {
			ConnectionManager var8 = this.minecraft.connectionManager;
			ArrayList var12 = new ArrayList();
			var12.add(var8.minecraft.user.name);
			Iterator var9 = var8.players.values().iterator();

			while(var9.hasNext()) {
				NetworkPlayer var15 = (NetworkPlayer)var9.next();
				var12.add(var15.name);
			}

			ArrayList var10 = var12;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
			GL11.glVertex2f((float)(var7 + 128), (float)(var11 - 68 - 12));
			GL11.glVertex2f((float)(var7 - 128), (float)(var11 - 68 - 12));
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
			GL11.glVertex2f((float)(var7 - 128), (float)(var11 + 68));
			GL11.glVertex2f((float)(var7 + 128), (float)(var11 + 68));
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			String var13 = "Connected players:";
			var1.drawShadow(var13, var7 - var1.width(var13) / 2, var11 - 64 - 12, 16777215);

			for(int var14 = 0; var14 < var10.size(); ++var14) {
				int var16 = var7 + var14 % 2 * 120 - 120;
				var5 = var11 - 64 + (var14 / 2 << 3);
				var1.draw((String)var10.get(var14), var16, var5, 16777215);
			}
		}

	}

	private static void blit(int var0, int var1, int var2, int var3, int var4, int var5) {
		float var7 = 0.00390625F;
		float var8 = 0.015625F;
		Tesselator var6 = Tesselator.instance;
		var6.begin();
		var6.vertexUV((float)var0, (float)(var1 + 22), -90.0F, 0.0F, (float)(var3 + 22) * var8);
		var6.vertexUV((float)(var0 + var4), (float)(var1 + 22), -90.0F, (float)(var4 + 0) * var7, (float)(var3 + 22) * var8);
		var6.vertexUV((float)(var0 + var4), (float)var1, -90.0F, (float)(var4 + 0) * var7, (float)var3 * var8);
		var6.vertexUV((float)var0, (float)var1, -90.0F, 0.0F, (float)var3 * var8);
		var6.end();
	}
}
