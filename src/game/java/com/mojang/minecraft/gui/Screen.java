package com.mojang.minecraft.gui;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.renderer.Tesselator;

import net.lax1dude.eaglercraft.opengl.Tessellator;
import net.lax1dude.eaglercraft.opengl.VertexFormat;
import net.lax1dude.eaglercraft.opengl.WorldRenderer;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Screen {
	protected Minecraft minecraft;
	protected int width;
	protected int height;
	protected List buttons = new ArrayList();

	public void render(int var1, int var2) {
		for(int var3 = 0; var3 < this.buttons.size(); ++var3) {
			Button var4 = (Button)this.buttons.get(var3);
			if(var4.visible) {
				if(!var4.enabled) {
					fill(var4.x - 1, var4.y - 1, var4.x + var4.w + 1, var4.y + var4.h + 1, -8355680);
					fill(var4.x, var4.y, var4.x + var4.w, var4.y + var4.h, -7303024);
					this.drawCenteredString(var4.msg, var4.x + var4.w / 2, var4.y + (var4.h - 8) / 2, -6250336);
				} else {
					fill(var4.x - 1, var4.y - 1, var4.x + var4.w + 1, var4.y + var4.h + 1, -16777216);
					if(var1 >= var4.x && var2 >= var4.y && var1 < var4.x + var4.w && var2 < var4.y + var4.h) {
						fill(var4.x - 1, var4.y - 1, var4.x + var4.w + 1, var4.y + var4.h + 1, -6250336);
						fill(var4.x, var4.y, var4.x + var4.w, var4.y + var4.h, -8355680);
						this.drawCenteredString(var4.msg, var4.x + var4.w / 2, var4.y + (var4.h - 8) / 2, 16777120);
					} else {
						fill(var4.x, var4.y, var4.x + var4.w, var4.y + var4.h, -9408400);
						this.drawCenteredString(var4.msg, var4.x + var4.w / 2, var4.y + (var4.h - 8) / 2, 14737632);
					}
				}
			}
		}

	}

	protected void keyPressed(char var1, int var2) {
		if(var2 == 1) {
			this.minecraft.setScreen((Screen)null);
			this.minecraft.grabMouse();
		}

	}
	
	protected void mousePressed(int var1, int var2, int var3) {
		if(var3 == 0) {
			for(var3 = 0; var3 < this.buttons.size(); ++var3) {
				Button var4 = (Button)this.buttons.get(var3);
				if(var4.enabled && var1 >= var4.x && var2 >= var4.y && var1 < var4.x + var4.w && var2 < var4.y + var4.h) {
					this.buttonClicked(var4);
				}
			}
		}

	}

	protected void buttonClicked(Button var1) {
	}

	public final void init(Minecraft var1, int var2, int var3) {
		this.minecraft = var1;
		this.width = var2;
		this.height = var3;
		this.init();
	}

	public void init() {
	}

	protected static void fill(int var0, int var1, int var2, int var3, int var4) {
		float var5 = (float)(var4 >>> 24) / 255.0F;
		float var6 = (float)(var4 >> 16 & 255) / 255.0F;
		float var7 = (float)(var4 >> 8 & 255) / 255.0F;
		float var9 = (float)(var4 & 255) / 255.0F;
		Tesselator var8 = Tesselator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(var6, var7, var9, var5);
		var8.begin();
		var8.vertex((float)var0, (float)var3, 0.0F);
		var8.vertex((float)var2, (float)var3, 0.0F);
		var8.vertex((float)var2, (float)var1, 0.0F);
		var8.vertex((float)var0, (float)var1, 0.0F);
		var8.end();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	static Tessellator tessellator = Tessellator.getInstance();
	static WorldRenderer renderer = tessellator.getWorldRenderer();
	
	protected static void fillGradient(int var0, int var1, int var2, int var3, int var4, int var5) {
		float var6 = (float) (var4 >>> 24) / 255.0F;
		float var7 = (float) (var4 >> 16 & 255) / 255.0F;
		float var8 = (float) (var4 >> 8 & 255) / 255.0F;
		float var12 = (float) (var4 & 255) / 255.0F;
		float var9 = (float) (var5 >>> 24) / 255.0F;
		float var10 = (float) (var5 >> 16 & 255) / 255.0F;
		float var11 = (float) (var5 >> 8 & 255) / 255.0F;
		float var13 = (float) (var5 & 255) / 255.0F;
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		renderer.begin(7, VertexFormat.POSITION_COLOR);
		renderer.pos((float) var2, (float) var1, 0.0f).color(var7, var8, var12, var6).endVertex();
		renderer.pos((float) var0, (float) var1, 0.0f).color(var7, var8, var12, var6).endVertex();
		renderer.pos((float) var0, (float) var3, 0.0f).color(var10, var11, var13, var9).endVertex();
		renderer.pos((float) var2, (float) var3, 0.0f).color(var10, var11, var13, var9).endVertex();
		tessellator.draw();
		GL11.glDisable(3042);
		GL11.glEnable(3553);
	}

	public final void drawCenteredString(String var1, int var2, int var3, int var4) {
		Font var5 = this.minecraft.font;
		var5.drawShadow(var1, var2 - var5.width(var1) / 2, var3, var4);
	}

	public final void drawString(String var1, int var2, int var3, int var4) {
		Font var5 = this.minecraft.font;
		var5.drawShadow(var1, var2, var3, var4);
	}

	public final void updateEvents() {
		while(Mouse.next()) {
			if(Mouse.getEventButtonState()) {
				int var1 = Mouse.getEventX() * this.width / this.minecraft.width;
				int var2 = this.height - Mouse.getEventY() * this.height / this.minecraft.height - 1;
				this.mousePressed(var1, var2, Mouse.getEventButton());
			}
		}

		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				this.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
		}

	}

	public void tick() {
	}

	public void closeScreen() {
	}
}
