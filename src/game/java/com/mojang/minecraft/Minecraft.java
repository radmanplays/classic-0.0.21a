package com.mojang.minecraft;

import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.character.ZombieModel;
import com.mojang.minecraft.gui.ChatScreen;
import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.gui.InGameHud;
import com.mojang.minecraft.gui.InventoryScreen;
import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.MovementInputFromOptions;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import com.mojang.minecraft.renderer.texture.TextureFX;
import com.mojang.minecraft.renderer.texture.TextureLavaFX;
import com.mojang.minecraft.renderer.texture.TextureWaterFX;
import com.mojang.util.GLAllocation;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EagUtils;
import com.mojang.minecraft.renderer.DirtyChunkSorter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.GZIPOutputStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public final class Minecraft implements Runnable {
	private boolean fullscreen = false;
	public int width;
	public int height;
	private FloatBuffer fogColor0 = GLAllocation.createFloatBuffer(4);
	private FloatBuffer fogColor1 = GLAllocation.createFloatBuffer(4);
	private Timer timer = new Timer(20.0F);
	public Level level;
	private LevelRenderer levelRenderer;
	public Player player;
	public int paintTexture = 1;
	private ParticleEngine particleEngine;
	public User user = null;
	private int yMouseAxis = 1;
	public Textures textures;
	public Font font;
	private int editMode = 0;
	public Screen screen = null;
	private LevelIO levelIo = new LevelIO(this);
	private LevelGen levelGen = new LevelGen(this);
	private int ticksRan = 0;
	public String loadMapUser = null;
	private InGameHud hud;
	public int loadMapID = 0;
	public ConnectionManager connectionManager;
	String server = null;
	int port = 0;
	private float fogColorRed = 0.5F;
	private float fogColorGreen = 0.8F;
	private float fogColorBlue = 1.0F;
	private volatile boolean running = false;
	public String fpsString = "";
	private boolean mouseGrabbed = false;
	private int prevFrameTime = 0;
	private float renderDistance = 0.0F;
	private IntBuffer viewportBuffer = GLAllocation.createIntBuffer(16);
	private IntBuffer selectBuffer = GLAllocation.createIntBuffer(2000);
	private HitResult hitResult = null;
	private float fogColorMultiplier = 1.0F;
	private volatile int unusedInt1 = 0;
	private volatile int unusedInt2 = 0;
	private FloatBuffer lb = GLAllocation.createFloatBuffer(16);
	private String title = "";
	private String text = "";
	public boolean hideGui = false;
	public ZombieModel playerModel = new ZombieModel();

	
	public Minecraft(int var2, int var3, boolean var4) {
		this.width = width;
		this.height = height;
		this.fullscreen = false;
		this.textures = new Textures();
		this.textures.registerTextureFX(new TextureLavaFX());
		this.textures.registerTextureFX(new TextureWaterFX());
	}
	
	public final void setServer(String var1) {
		server = var1;
	}
	
	public final void setScreen(Screen var1) {
		if(!(this.screen instanceof ErrorScreen)) {
			if(this.screen != null) {
				this.screen.closeScreen();
			}

			this.screen = var1;
			if(var1 != null) {
				if(this.mouseGrabbed) {
					this.player.releaseAllKeys();
					this.mouseGrabbed = false;
					Mouse.setGrabbed(false);
				}

				int var2 = this.width * 240 / this.height;
				int var3 = this.height * 240 / this.height;
				var1.init(this, var2, var3);
			} else {
				this.grabMouse();
			}
		}
	}
	
	private static void checkGlError(String string) {
		int errorCode = GL11.glGetError();
		if(errorCode != 0) {
			String errorString = GLU.gluErrorString(errorCode);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + string);
			System.out.println(errorCode + ": " + errorString);
			throw new RuntimeException(errorCode + ": " + errorString);

		}

	}

	public final void destroy() {
		Minecraft var2 = this;
		try {
			if(this.connectionManager == null && var2.level != null) {
				LevelIO.save(var2.level, new VFile2("level.dat"));
			}
		} catch (Exception var1) {
			var1.printStackTrace();
		}
		if(this.connectionManager != null) {
			connectionManager.connection.disconnect();
		}
		EagRuntime.destroy();
	}

	public final void run() {
		this.running = true;

		try {
			Minecraft var4 = this;
			this.fogColor0.put(new float[]{this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F});
			this.fogColor0.flip();
			this.fogColor1.put(new float[]{(float)14 / 255.0F, (float)11 / 255.0F, (float)10 / 255.0F, 1.0F});
			this.fogColor1.flip();
			if(this.fullscreen) {
				Display.toggleFullscreen();
				this.width = Display.getWidth();
				this.height = Display.getHeight();
			} else {
				this.width = Display.getWidth();
				this.height = Display.getHeight();
			}

			Display.setTitle("Minecraft 0.0.20a_02");

			Display.create();
			Keyboard.create();
			Mouse.create();

			checkGlError("Pre startup");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glClearDepth(1.0D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			checkGlError("Startup");
			this.font = new Font("/default.png", this.textures);
			IntBuffer var8 = GLAllocation.createIntBuffer(256);
			var8.clear().limit(256);
			GL11.glViewport(0, 0, this.width, this.height);
			if(this.server != null && this.user != null) {
				this.connectionManager = new ConnectionManager(this, this.server, this.user.name);
				this.level = null;
			} else {
				boolean var9 = false;
	
				try {
					Level var10 = null;
					var10 = var4.levelIo.load(new VFile2("level.dat"));
					var9 = var10 != null;
					if(!var9) {
						var10 = var4.levelIo.loadLegacy(new VFile2("level.dat"));
						var9 = var10 != null;
					}
	
					var4.setLevel(var10);
				} catch (Exception var20) {
					var20.printStackTrace();
					var9 = false;
				}
	
				if(!var9) {
					this.generateLevel(1);
				}
			}

			this.levelRenderer = new LevelRenderer(this.textures);
			this.particleEngine = new ParticleEngine(this.level, this.textures);
			this.player = new Player(this.level, new MovementInputFromOptions());
			this.player.resetPos();
			if(this.level != null) {
				this.setLevel(this.level);
			}

			checkGlError("Post startup");
			this.hud = new InGameHud(this, this.width, this.height);
		} catch (Exception var26) {
			var26.printStackTrace();
			System.out.println("Failed to start Minecraft");
			return;
		}

		long var1 = System.currentTimeMillis();
		int var3 = 0;

		try {
			while(this.running) {
					if(Display.isCloseRequested()) {
						if(this.connectionManager != null) {
							connectionManager.connection.disconnect();
						}
						this.running = false;
					}
					
					if(this.connectionManager != null) {
						ConnectionManager c = this.connectionManager;

							try {
								c.connection.processData();
							} catch (IOException var7) {
								c.minecraft.setScreen(new ErrorScreen("Disconnected!", "You\'ve lost connection to the server"));
								var7.printStackTrace();
								c.connection.disconnect();
								c.minecraft.connectionManager = null;
							}
					}

					try {
						Timer var39 = this.timer;
						long var7 = System.currentTimeMillis();
						long var43 = var7 - var39.lastSyncSysClock;
						long var11 = System.nanoTime() / 1000000L;
						double var15;
						if(var43 > 1000L) {
							long var13 = var11 - var39.lastSyncHRClock;
							var15 = (double)var43 / (double)var13;
							var39.timeSyncAdjustment += (var15 - var39.timeSyncAdjustment) * (double)0.2F;
							var39.lastSyncSysClock = var7;
							var39.lastSyncHRClock = var11;
						}

						if(var43 < 0L) {
							var39.lastSyncSysClock = var7;
							var39.lastSyncHRClock = var11;
						}

						double var48 = (double)var11 / 1000.0D;
						var15 = (var48 - var39.lastHRTime) * var39.timeSyncAdjustment;
						var39.lastHRTime = var48;
						if(var15 < 0.0D) {
							var15 = 0.0D;
						}

						if(var15 > 1.0D) {
							var15 = 1.0D;
						}

						var39.fps = (float)((double)var39.fps + var15 * (double)var39.timeScale * (double)var39.ticksPerSecond);
						var39.ticks = (int)var39.fps;
						if(var39.ticks > 100) {
							var39.ticks = 100;
						}

						var39.fps -= (float)var39.ticks;
						var39.a = var39.fps;

						for(int var40 = 0; var40 < this.timer.ticks; ++var40) {
							++this.ticksRan;
							this.tick();
						}

						checkGlError("Pre render");
						float var41 = this.timer.a;

						int var42;
						int var44;
						int var46;
						if(this.mouseGrabbed) {
							var42 = 0;
							var44 = 0;
							var42 = Mouse.getDX();
							var44 = Mouse.getDY();

							this.player.turn((float)var42, (float)(var44 * this.yMouseAxis));
						}

						if(!this.hideGui) {
							if(this.level != null) {
								this.render(var41);
								this.hud.render();
								checkGlError("Rendered gui");
							} else {
								GL11.glViewport(0, 0, this.width, this.height);
								GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
								GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
								GL11.glMatrixMode(GL11.GL_PROJECTION);
								GL11.glLoadIdentity();
								GL11.glMatrixMode(GL11.GL_MODELVIEW);
								GL11.glLoadIdentity();
								this.initGui();
							}

							if(this.screen != null) {
								var42 = this.width * 240 / this.height;
								var44 = this.height * 240 / this.height;
								int var47 = Mouse.getX() * var42 / this.width;
								var46 = var44 - Mouse.getY() * var44 / this.height - 1;
								this.screen.render(var47, var46);
							}
							Display.update();
						}

						checkGlError("Post render");
						++var3;
					} catch (Exception var34) {
						this.setScreen(new ErrorScreen("Client error", "The game broke! [" + var34 + "]"));
						var34.printStackTrace();
					}

					while(System.currentTimeMillis() >= var1 + 1000L) {
						this.fpsString = var3 + " fps, " + Chunk.updates + " chunk updates";
						Chunk.updates = 0;
						var1 += 1000L;
						var3 = 0;
					}
				}

			return;
		} catch (StopGameException var35) {
			return;
		} catch (Exception var36) {
			var36.printStackTrace();
		} finally {
			this.destroy();
		}

	}
	
	public final void grabMouse() {
		if(!this.mouseGrabbed) {
			this.mouseGrabbed = true;
			Mouse.setGrabbed(true);
			this.setScreen((Screen)null);
			this.prevFrameTime = this.ticksRan + 10000;
		}
	}
	
	private void pauseGame() {
		if(!(this.screen instanceof PauseScreen)) {
			this.setScreen(new PauseScreen());
		}
	}
	
	private int saveCountdown = 600;

	private void levelSave() {
	    if (level == null) return;

	    saveCountdown--;
	    if (saveCountdown <= 0) {
	    	LevelIO.save(this.level, new VFile2("level.dat"));
	        saveCountdown = 600;
	    }
	}
	

	private void clickMouse() {
		if(this.hitResult != null) {
			int var1 = this.hitResult.x;
			int var2 = this.hitResult.y;
			int var3 = this.hitResult.z;
			if(this.editMode != 0) {
				if(this.hitResult.f == 0) {
					--var2;
				}

				if(this.hitResult.f == 1) {
					++var2;
				}

				if(this.hitResult.f == 2) {
					--var3;
				}

				if(this.hitResult.f == 3) {
					++var3;
				}

				if(this.hitResult.f == 4) {
					--var1;
				}

				if(this.hitResult.f == 5) {
					++var1;
				}
			}

			Tile var4 = Tile.tiles[this.level.getTile(var1, var2, var3)];
			if(this.editMode == 0) {
				if(var4 != Tile.unbreakable || this.player.userType >= 100) {
					boolean var8 = this.level.netSetTile(var1, var2, var3, 0);
					if(var4 != null && var8) {
						if(this.isMultiplayer()) {
							this.connectionManager.sendBlockChange(var1, var2, var3, this.editMode, this.player.inventory.getSelected());
						}

						var4.destroy(this.level, var1, var2, var3, this.particleEngine);
					}

					return;
				}
			} else {
				int var5 = this.player.inventory.getSelected();
				var4 = Tile.tiles[this.level.getTile(var1, var2, var3)];
				if(var4 == null || var4 == Tile.water || var4 == Tile.calmWater || var4 == Tile.lava || var4 == Tile.calmLava) {
					AABB var7 = Tile.tiles[var5].getAABB(var1, var2, var3);
					if(var7 == null || (this.player.bb.intersects(var7) ? false : this.level.isFree(var7))) {
						if(this.isMultiplayer()) {
							this.connectionManager.sendBlockChange(var1, var2, var3, this.editMode, var5);
						}

						this.level.netSetTile(var1, var2, var3, this.player.inventory.getSelected());
						Tile.tiles[var5].onBlockAdded(this.level, var1, var2, var3);
					}
				}
			}

		}
	}
	
	private void tick() {
		InGameHud var1 = this.hud;
		for(int var2 = 0; var2 < var1.messages.size(); ++var2) {
			++((ChatLine)var1.messages.get(var2)).counter;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.getTextureId("/terrain.png"));
		Textures t = this.textures;

		for(int var2 = 0; var2 < t.textureList.size(); ++var2) {
			TextureFX var3 = (TextureFX)t.textureList.get(var2);
			var3.onTick();
			t.textureBuffer.clear();
			t.textureBuffer.put(var3.imageData);
			t.textureBuffer.position(0).limit(var3.imageData.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var3.iconIndex % 16 << 4, var3.iconIndex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)t.textureBuffer);
		}
		int var3;
		int var4;
		int var10;
		if(this.connectionManager != null) {
			Player var14 = this.player;
			ConnectionManager var9 = this.connectionManager;
			if(var9.isConnected()) {
				int var13 = (int)(var14.x * 32.0F);
				var4 = (int)(var14.y * 32.0F);
				int var5 = (int)(var14.z * 32.0F);
				int var6 = (int)(var14.yRot * 256.0F / 360.0F) & 255;
				int var2 = (int)(var14.xRot * 256.0F / 360.0F) & 255;
				var9.connection.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{Integer.valueOf(-1), Integer.valueOf(var13), Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6), Integer.valueOf(var2)});
			}
		}

		LevelRenderer var11;
		if(this.screen != null) {
			this.prevFrameTime = this.ticksRan + 10000;
		} else {
			if(Mouse.isMouseGrabbed() || Mouse.isActuallyGrabbed()) {
				this.mouseGrabbed = true;
			}
			label218:
			while(true) {
				while(Mouse.next()) {
					var10 = Mouse.getEventDWheel();
					if(var10 != 0) {
						int var2 = var10;
						Inventory inv = this.player.inventory;
						if(var10 > 0) {
							var2 = 1;
						}

						if(var2 < 0) {
							var2 = -1;
						}

						for(inv.selectedSlot -= var2; inv.selectedSlot < 0; inv.selectedSlot += inv.slots.length) {
						}

						while(inv.selectedSlot >= inv.slots.length) {
							inv.selectedSlot -= inv.slots.length;
						}
					}

					if(!this.mouseGrabbed && Mouse.getEventButtonState()) {
						this.grabMouse();
					} else {
						if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
							this.clickMouse();
							this.prevFrameTime = this.ticksRan;
						}

						if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
							this.editMode = (this.editMode + 1) % 2;
						}

						if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.hitResult != null) {
							int var2 = this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z);
							if(var2 == Tile.grass.id) {
								var2 = Tile.dirt.id;
							}

							Inventory var15 = this.player.inventory;
							int var5 = var15.getSlotContainsID(var2);
							if(var5 >= 0) {
								var15.selectedSlot = var5;
							} else if(var2 > 0 && User.creativeTiles.contains(Tile.tiles[var2])) {
								var15.getSlotContainsTile(Tile.tiles[var2]);
							}
						}
					}
				}

				while(true) {
					do {
						if(!Keyboard.next()) {
							if(Mouse.isButtonDown(0) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.mouseGrabbed) {
								this.clickMouse();
								this.prevFrameTime = this.ticksRan;
							}
							break label218;
						}

						this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
					} while(!Keyboard.getEventKeyState());

					if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
						this.pauseGame();
					}

					if(Keyboard.getEventKey() == Keyboard.KEY_R) {
						this.player.resetPos();
					}

					if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
						this.level.setSpawnPos((int)this.player.x, (int)this.player.y, (int)this.player.z, this.player.yRot);
						this.player.resetPos();
					}

					for(var10 = 0; var10 < 9; ++var10) {
						if(Keyboard.getEventKey() == var10 + 2) {
							this.player.inventory.selectedSlot = var10;
						}
					}

					if(Keyboard.getEventKey() == Keyboard.KEY_Y) {
						this.yMouseAxis = -this.yMouseAxis;
					}

					if(Keyboard.getEventKey() == Keyboard.KEY_G && this.connectionManager == null && this.level.entities.size() < 256) {
						this.level.entities.add(new Zombie(this.level, this.player.x, this.player.y, this.player.z));
					}

					if(Keyboard.getEventKey() == Keyboard.KEY_F) {
						LevelRenderer var10000 = this.levelRenderer;
						boolean var19 = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
						LevelRenderer var16 = var10000;
						var16.drawDistance = var16.drawDistance + (var19 ? -1 : 1) & 3;
					}

					if(Keyboard.getEventKey() == Keyboard.KEY_B) {
						this.setScreen(new InventoryScreen());
					}

					if(Keyboard.getEventKey() == Keyboard.KEY_T && this.connectionManager != null && this.connectionManager.isConnected()) {
						this.player.releaseAllKeys();
						this.setScreen(new ChatScreen());
					}
				}
			}
		}

		if(this.screen != null) {
			this.screen.updateEvents();
			if(this.screen != null) {
				this.screen.tick();
			}
		}
		
		if(this.connectionManager != null) {
			this.connectionManager.tick();
		}
			if(this.level != null) {
			LevelRenderer var16 = this.levelRenderer;
			++var16.cloudTickCounter;
			this.level.tickEntities();
			if(!this.isMultiplayer()) {
				this.level.tick();
			}

			ParticleEngine var18 = this.particleEngine;

			for(int var2 = 0; var2 < var18.particles.size(); ++var2) {
				Particle var17 = (Particle)var18.particles.get(var2);
				var17.tick();
				if(var17.removed) {
					var18.particles.remove(var2--);
				}
			}

			this.player.tick();
			if(this.connectionManager == null) {
				levelSave();
			}
		}
	}
	
	private boolean isMultiplayer() {
		return this.connectionManager != null;
	}

	private void orientCamera(float var1) {
		GL11.glTranslatef(0.0F, 0.0F, -0.3F);
		GL11.glRotatef(this.player.xRotO + (this.player.xRot - this.player.xRotO) * var1, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(this.player.yRotO + (this.player.yRot - this.player.yRotO) * var1, 0.0F, 1.0F, 0.0F);
		float var2 = this.player.xo + (this.player.x - this.player.xo) * var1;
		float var3 = this.player.yo + (this.player.y - this.player.yo) * var1;
		float var4 = this.player.zo + (this.player.z - this.player.zo) * var1;
		GL11.glTranslatef(-var2, -var3, -var4);
	}

	private void render(float var1) {
		if (Display.wasResized()) {
			if(Display.getHeight() != 0) {
				this.width = Display.getWidth();
				this.height = Display.getHeight();
				if(this.hud !=null) {
					this.hud = new InGameHud(this, this.width, this.height);
				}
				
				if(this.screen != null) {
					Screen sc = this.screen;
					this.setScreen((Screen)null);
					this.setScreen(sc);
				}
			}
		}
		GL11.glViewport(0, 0, this.width, this.height);
		float var4 = 1.0F / (float)(4 - this.levelRenderer.drawDistance);
		var4 = (float)Math.pow((double)var4, 0.25D);
		this.fogColorRed = 0.6F * (1.0F - var4) + var4;
		this.fogColorGreen = 0.8F * (1.0F - var4) + var4;
		this.fogColorBlue = 1.0F * (1.0F - var4) + var4;
		this.fogColorRed *= this.fogColorMultiplier;
		this.fogColorGreen *= this.fogColorMultiplier;
		this.fogColorBlue *= this.fogColorMultiplier;
		Tile var5 = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)];
		if(var5 != null && var5.getLiquidType() != Liquid.none) {
			Liquid var22 = var5.getLiquidType();
			if(var22 == Liquid.water) {
				this.fogColorRed = 0.02F;
				this.fogColorGreen = 0.02F;
				this.fogColorBlue = 0.2F;
			} else if(var22 == Liquid.lava) {
				this.fogColorRed = 0.6F;
				this.fogColorGreen = 0.1F;
				this.fogColorBlue = 0.0F;
			}
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		checkGlError("Set viewport");
		float pitch = this.player.xRot;
		float yaw = this.player.yRot;

		double px = this.player.x;
		double py = this.player.y;
		double pz = this.player.z;

		Vec3 cameraPos = new Vec3((float)px, (float)py, (float)pz);

		float cosYaw = (float)Math.cos(-Math.toRadians(yaw) - Math.PI);
		float sinYaw = (float)Math.sin(-Math.toRadians(yaw) - Math.PI);
		float cosPitch = (float)Math.cos(-Math.toRadians(pitch));
		float sinPitch = (float)Math.sin(-Math.toRadians(pitch));

		float dirX = sinYaw * cosPitch;
		float dirY = sinPitch;
		float dirZ = cosYaw * cosPitch;
		float reachDistance = 3.0F;
		if (pitch > 60.0F) {
		    reachDistance += 1.0F;
		}
		if (pitch >= 55.0F && pitch <= 60.0F) {
		    reachDistance += 2.0F;
		}
		Vec3 reachVec = new Vec3(
		    cameraPos.x + dirX * reachDistance,
		    cameraPos.y + dirY * reachDistance,
		    cameraPos.z + dirZ * reachDistance
		);

		this.hitResult = this.level.clip(cameraPos, reachVec);
		checkGlError("Picked");
		this.fogColorMultiplier = 1.0F;
		this.renderDistance = (float)(512 >> (this.levelRenderer.drawDistance << 1));
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, this.renderDistance);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
	    if (!Display.isActive() || !Mouse.isMouseGrabbed() || !Mouse.isActuallyGrabbed()) {
	        if (System.currentTimeMillis() - prevFrameTime > 250L) {
	            if (this.screen == null) {
	            	this.pauseGame();
	            }
	        }
	    }
	    
		this.orientCamera(var1);
		checkGlError("Set up camera");
		GL11.glEnable(GL11.GL_CULL_FACE);
		Frustum var23 = Frustum.getFrustum();
		Frustum var24 = var23;
		LevelRenderer lr = this.levelRenderer;

		for(int i = 0; i < lr.sortedChunks.length; ++i) {
			lr.sortedChunks[i].isInFrustum(var24);
		}

		Player var19 = this.player;
		lr = this.levelRenderer;
		List<Chunk> var28 = new ArrayList<>(lr.dirtyChunks);
		var28.sort(new DirtyChunkSorter(var19));
		var28.addAll(lr.dirtyChunks);
		int var25 = 4;
		Iterator var29 = var28.iterator();

		while(var29.hasNext()) {
			Chunk var30 = (Chunk)var29.next();
			var30.rebuild();
			lr.dirtyChunks.remove(var30);
			--var25;
			if(var25 == 0) {
				break;
			}
		}


		checkGlError("Update chunks");
		boolean var21 = this.level.isSolid(this.player.x, this.player.y, this.player.z, 0.1F);
		this.setupFog();
		GL11.glEnable(GL11.GL_FOG);
		this.levelRenderer.render(this.player, 0);
		if(var21) {
			int x = (int)this.player.x;
			int y = (int)this.player.y;
			var25 = (int)this.player.z;

			for(int var2 = x - 1; var2 <= x + 1; ++var2) {
				for(int var7 = y - 1; var7 <= y + 1; ++var7) {
					for(int var8 = var25 - 1; var8 <= var25 + 1; ++var8) {
						this.levelRenderer.render(var2, var7, var8);
					}
				}
			}
		}

		checkGlError("Rendered level");
		this.levelRenderer.renderEntities(var23, var1);
		checkGlError("Rendered entities");
		this.particleEngine.render(this.player, var1);
		checkGlError("Rendered particles");
		lr = this.levelRenderer;
		lr.renderSurroundingGround();
		GL11.glDisable(GL11.GL_LIGHTING);
		this.setupFog();
		this.levelRenderer.renderClouds(var1);
		this.setupFog();
		GL11.glEnable(GL11.GL_LIGHTING);
		if(this.hitResult != null) {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.player.inventory.getSelected());
			LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_LIGHTING);
		}

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.setupFog();
		lr = this.levelRenderer;
//		lr.renderSurroundingGround();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//		GL11.glColorMask(false, false, false, false);
		int var22 = this.levelRenderer.render(this.player, 1);
//		GL11.glColorMask(true, true, true, true);
		if(var22 > 0) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, lr.textures.getTextureId("/terrain.png"));
			GL11.glCallLists(lr.dummyBuffer);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if(this.hitResult != null) {
			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
//			this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.player.inventory.getSelected());
			LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}
	}
	
	public void initGui() {
		int var1 = this.width * 240 / this.height;
		int var2 = this.height * 240 / this.height;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)var1, (double)var2, 0.0D, 100.0D, 300.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
	}

	
	private void setupFog() {
		GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
		Tile var1 = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)];
		if(var1 != null && var1.getLiquidType() != Liquid.none) {
			Liquid var2 = var1.getLiquidType();
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if(var2 == Liquid.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
//				GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.4F, 0.9F, 1.0F));
			} else if(var2 == Liquid.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
//				GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.3F, 0.3F, 1.0F));
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.renderDistance);
//			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
		}

//		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
//		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private FloatBuffer getBuffer(float a, float b, float c, float d) {
		this.lb.clear();
		this.lb.put(a).put(b).put(c).put(d);
		this.lb.flip();
		return this.lb;
	}

	public final void beginLevelLoading(String var1) {
		if(!this.running) {
			throw new StopGameException();
		} else {
			this.title = var1;
		    if (this.height == 0) {
		        return;
		    }
			int var3 = this.width * 240 / this.height;
			int var2 = this.height * 240 / this.height;
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)var3, (double)var2, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public final void levelLoadUpdate(String var1) {
		if(!this.running) {
			throw new StopGameException();
		} else {
			this.text = var1;
			this.setLoadingProgress(-1);
		}
	}

	public final void setLoadingProgress(int var1) {
		if(!this.running) {
			throw new StopGameException();
		} else {
			int var2 = this.width * 240 / this.height;
			int var3 = this.height * 240 / this.height;
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			Tesselator var4 = Tesselator.instance;
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			int var5 = this.textures.getTextureId("/dirt.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
			float var8 = 32.0F;
			var4.begin();
			var4.color(4210752);
			var4.vertexUV(0.0F, (float)var3, 0.0F, 0.0F, (float)var3 / var8);
			var4.vertexUV((float)var2, (float)var3, 0.0F, (float)var2 / var8, (float)var3 / var8);
			var4.vertexUV((float)var2, 0.0F, 0.0F, (float)var2 / var8, 0.0F);
			var4.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			var4.end();
			if(var1 >= 0) {
				var5 = var2 / 2 - 50;
				int var6 = var3 / 2 + 16;
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				var4.begin();
				var4.color(8421504);
				var4.vertex((float)var5, (float)var6, 0.0F);
				var4.vertex((float)var5, (float)(var6 + 2), 0.0F);
				var4.vertex((float)(var5 + 100), (float)(var6 + 2), 0.0F);
				var4.vertex((float)(var5 + 100), (float)var6, 0.0F);
				var4.color(8454016);
				var4.vertex((float)var5, (float)var6, 0.0F);
				var4.vertex((float)var5, (float)(var6 + 2), 0.0F);
				var4.vertex((float)(var5 + var1), (float)(var6 + 2), 0.0F);
				var4.vertex((float)(var5 + var1), (float)var6, 0.0F);
				var4.end();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			this.font.drawShadow(this.title, (var2 - this.font.width(this.title)) / 2, var3 / 2 - 4 - 16, 16777215);
			this.font.drawShadow(this.text, (var2 - this.font.width(this.text)) / 2, var3 / 2 - 4 + 8, 16777215);
			Display.update();
		}
	}

	public final void generateLevel(int var1) {
		String var2 = this.user != null ? this.user.name : "anonymous";
		this.setLevel(this.levelGen.generateLevel(var2, 128 << var1, 128 << var1, 64));
	}

	public final void setLevel(Level var1) {
		this.level = var1;
		if(this.levelRenderer != null) {
			LevelRenderer var2 = this.levelRenderer;
			if(var2.level != null) {
				var2.level.removeListener(var2);
			}

			var2.level = var1;
			if(var1 != null) {
				var1.addListener(var2);
				var2.compileSurroundingGround();
			}
		}

		if(this.particleEngine != null) {
			ParticleEngine var4 = this.particleEngine;
			var4.particles.clear();
		}

		if(this.player != null) {
			this.player.setLevel(var1);
			this.player.resetPos();
		}

		System.gc();
	}
	
	public final void addChatMessage(String var1) {
		InGameHud var2 = this.hud;
		var2.messages.add(0, new ChatLine(var1));

		while(var2.messages.size() > 50) {
			var2.messages.remove(var2.messages.size() - 1);
		}

	}
}
