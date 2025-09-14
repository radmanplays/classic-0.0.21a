package com.mojang.minecraft.gui;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public final class InventoryScreen extends Screen {
	public InventoryScreen() {
		this.allowUserInput = true;
	}

	private int getTileAtSlot(int var1, int var2) {
		for(int var3 = 0; var3 < User.creativeTiles.size(); ++var3) {
			int var4 = this.width / 2 + var3 % 8 * 24 - 96 - 3;
			int var5 = this.height / 2 + var3 / 8 * 24 - 48 + 3;
			if(var1 >= var4 && var1 <= var4 + 24 && var2 >= var5 - 12 && var2 <= var5 + 12) {
				return var3;
			}
		}

		return -1;
	}

	public final void render(int var1, int var2) {
		var1 = this.getTileAtSlot(var1, var2);
		fillGradient(this.width / 2 - 120, 30, this.width / 2 + 120, 180, -1878719232, -1070583712);
		if(var1 >= 0) {
			var2 = this.width / 2 + var1 % 8 * 24 - 96;
			int var3 = this.height / 2 + var1 / 8 * 24 - 48;
			fillGradient(var2 - 3, var3 - 8, var2 + 23, var3 + 24 - 6, -1862270977, -1056964609);
		}

		this.drawCenteredString("Select block", this.width / 2, 40, 16777215);
		Textures var7 = this.minecraft.textures;
		Tesselator var8 = Tesselator.instance;
		var2 = var7.getTextureId("/terrain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		for(var2 = 0; var2 < User.creativeTiles.size(); ++var2) {
			Tile var4 = (Tile)User.creativeTiles.get(var2);
			GL11.glPushMatrix();
			int var5 = this.width / 2 + var2 % 8 * 24 - 96;
			int var6 = this.height / 2 + var2 / 8 * 24 - 48;
			GL11.glTranslatef((float)var5, (float)var6, 0.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 8.0F);
			GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			if(var1 == var2) {
				GL11.glScalef(1.6F, 1.6F, 1.6F);
			}

			GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
			GL11.glScalef(-1.0F, -1.0F, -1.0F);
			var8.begin();
			var4.render(var8, this.minecraft.level, 0, -2, 0, 0);
			var8.end();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	protected final void mousePressed(int var1, int var2, int var3) {
		if(var3 == 0) {
			Inventory var10000 = this.minecraft.player.inventory;
			var2 = this.getTileAtSlot(var1, var2);
			Inventory var4 = var10000;
			if(var2 >= 0) {
				var4.getSlotContainsTile((Tile)User.creativeTiles.get(var2));
			}

			this.minecraft.setScreen((Screen)null);
		}

	}
}
