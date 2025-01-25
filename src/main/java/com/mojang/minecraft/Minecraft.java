package com.mojang.minecraft;

import com.mojang.comm.SocketConnection;
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
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.MovementInputFromOptions;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.DirtyChunkSorter;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import com.mojang.minecraft.renderer.texture.TextureFX;
import com.mojang.minecraft.renderer.texture.TextureLavaFX;
import com.mojang.minecraft.renderer.texture.TextureWaterFX;

import net.lax1dude.eaglercraft.adapter.RealOpenGLEnums;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.TreeSet;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public final class Minecraft implements Runnable {
	private boolean fullscreen = false;
	public int width;
	public int height;
	private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(20.0F);
	public Level level;
	private LevelRenderer levelRenderer;
	public Player player;
	private ParticleEngine particleEngine;
	public User user = null;
	public String minecraftUri;
	public boolean appletMode = false;
	public volatile boolean pause = false;
	private int yMouseAxis = 1;
	public Textures textures;
	public Font font;
	private int editMode = 0;
	public Screen screen = null;
	public LevelIO levelIo = new LevelIO(this);
	private LevelGen levelGen = new LevelGen(this);
	private int ticksRan = 0;
	public String loadMapUser = null;
	public int loadMapId = 0;
	private InGameHud hud;
	public ConnectionManager connectionManager;
	String server = null;
	int port = 0;
	private float fogColorRed = 0.5F;
	private float fogColorGreen = 0.8F;
	private float fogColorBlue = 1.0F;
	volatile boolean running = false;
	public String fpsString = "";
	private boolean mouseGrabbed = false;
	private int prevFrameTime = 0;
	private float renderDistance = 0.0F;
	private HitResult hitResult = null;
	private float fogColorMultiplier = 1.0F;
	private boolean displayActive = false;
	private volatile int unusedInt1 = 0;
	private volatile int unusedInt2 = 0;
	private FloatBuffer lb = BufferUtils.createFloatBuffer(16);
	private String title = "";
	private String text = "";
	public boolean hideGui = false;
	public ZombieModel playerModel = new ZombieModel();

	public Minecraft(int var2, int var3, boolean var4) {
		this.width = var2;
		this.height = var3;
		this.fullscreen = false;
		this.textures = new Textures();
		this.textures.registerTextureFX(new TextureLavaFX());
		this.textures.registerTextureFX(new TextureWaterFX());
		

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

	private static void checkGlError(String var0) {
		int var1 = GL11.glGetError();
		if(var1 != 0) {
			String var2 = GLU.gluErrorString(var1);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + var0);
			System.out.println(var1 + ": " + var2);
			//System.exit(0);
		}

	}

	public final void destroy() {
		Minecraft var2 = this;
		if(!var2.appletMode) {
			try {
				LevelIO.save(var2.level, new FileOutputStream(new File("level.dat")));
			} catch (Exception var1) {
				var1.printStackTrace();
			}
		}

		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
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
				Display.setFullscreen(true);
				this.width = Display.getDisplayMode().getWidth();
				this.height = Display.getDisplayMode().getHeight();
			} else {
				Display.setDisplayMode(new DisplayMode(this.width, this.height));
			}

			Display.setTitle("Minecraft 0.0.21a");

			try {
				Display.create();
			} catch (LWJGLException var31) {
				var31.printStackTrace();

				try {
					Thread.sleep(1000L);
				} catch (InterruptedException var30) {
				}

				Display.create();
			}

			Keyboard.create();
			Mouse.create();

			try {
				Controllers.create();
			} catch (Exception var29) {
				var29.printStackTrace();
			}

			checkGlError("Pre startup");
			GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			GL11.glShadeModel(RealOpenGLEnums.GL_SMOOTH);
			GL11.glClearDepth(1.0D);
			GL11.glEnable(RealOpenGLEnums.GL_DEPTH_TEST);
			GL11.glDepthFunc(RealOpenGLEnums.GL_LEQUAL);
			GL11.glEnable(RealOpenGLEnums.GL_ALPHA_TEST);
			GL11.glAlphaFunc(RealOpenGLEnums.GL_GREATER, 0.0F);
			GL11.glCullFace(RealOpenGLEnums.GL_BACK);
			GL11.glMatrixMode(RealOpenGLEnums.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(RealOpenGLEnums.GL_MODELVIEW);
			checkGlError("Startup");
			this.font = new Font("/default.png");
			IntBuffer var8 = BufferUtils.createIntBuffer(256);
			var8.clear().limit(256);
			GL11.glViewport(0, 0, this.width, this.height);
			if(this.server != null && this.user != null) {
				this.connectionManager = new ConnectionManager(this, this.server, this.port, this.user.name, this.user.mpPass);
				this.level = null;
			} else {
				boolean var9 = false;

				try {
					if(var4.loadMapUser != null) {
						var9 = var4.loadLevel(var4.loadMapUser, var4.loadMapId);
					} else if(!var4.appletMode) {
						Level var10 = null;
						var10 = var4.levelIo.load(new FileInputStream(new File("level.dat")));
						var9 = var10 != null;
						if(!var9) {
							var10 = var4.levelIo.loadLegacy(new FileInputStream(new File("level.dat")));
							var9 = var10 != null;
						}

						var4.setLevel(var10);
					}
				} catch (Exception var28) {
					var28.printStackTrace();
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
		} catch (Exception var36) {
			var36.printStackTrace();
			return;
		}

		long var1 = System.currentTimeMillis();
		int var3 = 0;

		try {
			while(this.running) {
				if(this.pause) {
					Thread.sleep(100L);
				} else {
					if(Display.isCloseRequested()) {
						this.running = false;
					}

					try {
						Timer var37 = this.timer;
						long var7 = System.currentTimeMillis();
						long var41 = var7 - var37.lastSyncSysClock;
						long var11 = System.nanoTime() / 1000000L;
						double var15;
						if(var41 > 1000L) {
							long var13 = var11 - var37.lastSyncHRClock;
							var15 = (double)var41 / (double)var13;
							var37.timeSyncAdjustment += (var15 - var37.timeSyncAdjustment) * (double)0.2F;
							var37.lastSyncSysClock = var7;
							var37.lastSyncHRClock = var11;
						}

						if(var41 < 0L) {
							var37.lastSyncSysClock = var7;
							var37.lastSyncHRClock = var11;
						}

						double var46 = (double)var11 / 1000.0D;
						var15 = (var46 - var37.lastHRTime) * var37.timeSyncAdjustment;
						var37.lastHRTime = var46;
						if(var15 < 0.0D) {
							var15 = 0.0D;
						}

						if(var15 > 1.0D) {
							var15 = 1.0D;
						}

						var37.fps = (float)((double)var37.fps + var15 * (double)var37.timeScale * (double)var37.ticksPerSecond);
						var37.ticks = (int)var37.fps;
						if(var37.ticks > 100) {
							var37.ticks = 100;
						}

						var37.fps -= (float)var37.ticks;
						var37.a = var37.fps;

						for(int var38 = 0; var38 < this.timer.ticks; ++var38) {
							++this.ticksRan;
							this.tick();
						}

						checkGlError("Pre render");
						float var39 = this.timer.a;
						if(this.displayActive && !Display.isActive()) {
							this.pauseGame();
						}

						this.displayActive = Display.isActive();
						int var40;
						int var42;
						int var44;
						if(this.mouseGrabbed) {
							var40 = 0;
							var42 = 0;
						
								var40 = Mouse.getDX();
								var42 = Mouse.getDY();
							

							this.player.turn((float)var40, (float)(var42 * this.yMouseAxis));
						}

						if(!this.hideGui) {
							if(this.level != null) {
								this.render(var39);
								this.hud.render();
								checkGlError("Rendered gui");
							} else {
								GL11.glViewport(0, 0, this.width, this.height);
								GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
								GL11.glClear(RealOpenGLEnums.GL_DEPTH_BUFFER_BIT | RealOpenGLEnums.GL_COLOR_BUFFER_BIT);
								GL11.glMatrixMode(RealOpenGLEnums.GL_PROJECTION);
								GL11.glLoadIdentity();
								GL11.glMatrixMode(RealOpenGLEnums.GL_MODELVIEW);
								GL11.glLoadIdentity();
								this.initGui();
							}

							if(this.screen != null) {
								var40 = this.width * 240 / this.height;
								var42 = this.height * 240 / this.height;
								int var45 = Mouse.getX() * var40 / this.width;
								var44 = var42 - Mouse.getY() * var42 / this.height - 1;
								this.screen.render(var45, var44);
							}

							Display.update();
						}

						checkGlError("Post render");
						++var3;
					} catch (Exception var32) {
						this.setScreen(new ErrorScreen("Client error", "The game broke! [" + var32 + "]"));
						var32.printStackTrace();
					}

					while(System.currentTimeMillis() >= var1 + 1000L) {
						this.fpsString = var3 + " fps, " + Chunk.updates + " chunk updates";
						Chunk.updates = 0;
						var1 += 1000L;
						var3 = 0;
					}
				}
			}

			return;
		} catch (StopGameException var33) {
		} catch (Exception var34) {
			var34.printStackTrace();
			return;
		} finally {
			this.destroy();
		}

	}

	public final void grabMouse() {
		if(!this.mouseGrabbed) {
			this.mouseGrabbed = true;
			if(this.appletMode) {
					Mouse.setCursorPosition(this.width / 2, this.height / 2);
				
			} else {
				Mouse.setGrabbed(true);
			}

			this.setScreen((Screen)null);
			this.prevFrameTime = this.ticksRan + 10000;
		}
	}

	private void pauseGame() {
		if(!(this.screen instanceof PauseScreen)) {
			this.setScreen(new PauseScreen());
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
					AABB var7 = Tile.tiles[var5].getTileAABB(var1, var2, var3);
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

		int var2;
		for(var2 = 0; var2 < var1.messages.size(); ++var2) {
			++((ChatLine)var1.messages.get(var2)).counter;
		}

		GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, this.textures.getTextureId("/terrain.png"));
		Textures var8 = this.textures;

		for(var2 = 0; var2 < var8.textureList.size(); ++var2) {
			TextureFX var3 = (TextureFX)var8.textureList.get(var2);
			var3.onTick();
			var8.textureBuffer.clear();
			var8.textureBuffer.put(var3.imageData);
			var8.textureBuffer.position(0).limit(var3.imageData.length);
			GL11.glTexSubImage2D(RealOpenGLEnums.GL_TEXTURE_2D, 0, var3.iconIndex % 16 << 4, var3.iconIndex / 16 << 4, 16, 16, RealOpenGLEnums.GL_RGBA, RealOpenGLEnums.GL_UNSIGNED_BYTE, (ByteBuffer)var8.textureBuffer);
		}

		int var5;
		if(this.connectionManager != null) {
			if(!this.connectionManager.isConnected()) {
				this.beginLevelLoading("Connecting..");
				this.setLoadingProgress(0);
			} else {
				ConnectionManager var9 = this.connectionManager;
				if(var9.processData) {
					SocketConnection var12 = var9.connection;
					if(var12.connected) {
						try {
							var9.connection.processData();
						} catch (Exception var7) {
							var9.minecraft.setScreen(new ErrorScreen("Disconnected!", "You\'ve lost connection to the server"));
							var9.minecraft.hideGui = false;
							var7.printStackTrace();
							var9.connection.disconnect();
							var9.minecraft.connectionManager = null;
						}
					}
				}

				Player var14 = this.player;
				var9 = this.connectionManager;
				if(var9.connected) {
					int var13 = (int)(var14.x * 32.0F);
					int var4 = (int)(var14.y * 32.0F);
					var5 = (int)(var14.z * 32.0F);
					int var6 = (int)(var14.yRot * 256.0F / 360.0F) & 255;
					var2 = (int)(var14.xRot * 256.0F / 360.0F) & 255;
					//var9.connection.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{Integer.valueOf(-1), Integer.valueOf(var13), Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6), Integer.valueOf(var2)});
				}
			}
		}

		LevelRenderer var16;
		if(this.screen == null || this.screen.allowUserInput) {
			label251:
			while(true) {
				int var10;
				if(!Mouse.next()) {
					while(true) {
						do {
							do {
								if(!Keyboard.next()) {
									if(this.screen == null && Mouse.isButtonDown(0) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.mouseGrabbed) {
										this.clickMouse();
										this.prevFrameTime = this.ticksRan;
									}
									break label251;
								}

								this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
							} while(!Keyboard.getEventKeyState());

							if(this.screen != null) {
								this.screen.updateKeyboardEvents();
							}

							if(this.screen == null) {
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

								if(Keyboard.getEventKey() == Keyboard.KEY_G && this.connectionManager == null && this.level.entities.size() < 256) {
									this.level.entities.add(new Zombie(this.level, this.player.x, this.player.y, this.player.z));
								}

								if(Keyboard.getEventKey() == Keyboard.KEY_B) {
									this.setScreen(new InventoryScreen());
								}


								if(Keyboard.getEventKey() == Keyboard.KEY_T && this.connectionManager != null && this.connectionManager.isConnected()) {
									this.player.releaseAllKeys();
									this.setScreen(new ChatScreen());
								}
							}

							for(var10 = 0; var10 < 9; ++var10) {
								if(Keyboard.getEventKey() == var10 + 2) {
									this.player.inventory.selectedSlot = var10;
								}
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_Y) {
								this.yMouseAxis = -this.yMouseAxis;
							}
						} while(Keyboard.getEventKey() != Keyboard.KEY_F);

						LevelRenderer var10000 = this.levelRenderer;
						boolean var20 = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
						var16 = var10000;
						var16.drawDistance = var16.drawDistance + (var20 ? -1 : 1) & 3;
					}
				}

				var10 = Mouse.getEventDWheel();
				if(var10 != 0) {
					var2 = var10;
					Inventory var11 = this.player.inventory;
					if(var10 > 0) {
						var2 = 1;
					}

					if(var2 < 0) {
						var2 = -1;
					}

					for(var11.selectedSlot -= var2; var11.selectedSlot < 0; var11.selectedSlot += var11.slots.length) {
					}

					while(var11.selectedSlot >= var11.slots.length) {
						var11.selectedSlot -= var11.slots.length;
					}
				}

				if(this.screen == null) {
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
							var2 = this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z);
							if(var2 == Tile.grass.id) {
								var2 = Tile.dirt.id;
							}

							Inventory var15 = this.player.inventory;
							var5 = var15.getSlotContainsID(var2);
							if(var5 >= 0) {
								var15.selectedSlot = var5;
							} else if(var2 > 0 && User.creativeTiles.contains(Tile.tiles[var2])) {
								var15.getSlotContainsTile(Tile.tiles[var2]);
							}
						}
					}
				}

				if(this.screen != null) {
					this.screen.updateMouseEvents();
				}
			}
		}

		if(this.screen != null) {
			this.prevFrameTime = this.ticksRan + 10000;
		}

		if(this.screen != null) {
			Screen var17 = this.screen;

			while(Mouse.next()) {
				var17.updateMouseEvents();
			}

			while(Keyboard.next()) {
				var17.updateKeyboardEvents();
			}

			if(this.screen != null) {
				this.screen.tick();
			}
		}

		if(this.level != null) {
			var16 = this.levelRenderer;
			++var16.cloudTickCounter;
			this.level.tickEntities();
			if(!this.isMultiplayer()) {
				this.level.tick();
			}

			ParticleEngine var19 = this.particleEngine;

			for(var2 = 0; var2 < var19.particles.size(); ++var2) {
				Particle var18 = (Particle)var19.particles.get(var2);
				var18.tick();
				if(var18.removed) {
					var19.particles.remove(var2--);
				}
			}

			this.player.tick();
		}

	}

	private boolean isMultiplayer() {
		return this.connectionManager != null;
	}

	private void render(float var1) {
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
			Liquid var6 = var5.getLiquidType();
			if(var6 == Liquid.water) {
				this.fogColorRed = 0.02F;
				this.fogColorGreen = 0.02F;
				this.fogColorBlue = 0.2F;
			} else if(var6 == Liquid.lava) {
				this.fogColorRed = 0.6F;
				this.fogColorGreen = 0.1F;
				this.fogColorBlue = 0.0F;
			}
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
		GL11.glClear(RealOpenGLEnums.GL_DEPTH_BUFFER_BIT | RealOpenGLEnums.GL_COLOR_BUFFER_BIT);
		checkGlError("Set viewport");
		float var13 = this.player.xRotO + (this.player.xRot - this.player.xRotO) * var1;
		float var17 = this.player.yRotO + (this.player.yRot - this.player.yRotO) * var1;
		float var7 = this.player.xo + (this.player.x - this.player.xo) * var1;
		float var8 = this.player.yo + (this.player.y - this.player.yo) * var1;
		float var2 = this.player.zo + (this.player.z - this.player.zo) * var1;
		Vec3 var9 = new Vec3(var7, var8, var2);
		var4 = (float)Math.cos((double)(-var17) * Math.PI / 180.0D + Math.PI);
		var17 = (float)Math.sin((double)(-var17) * Math.PI / 180.0D + Math.PI);
		var7 = (float)Math.cos((double)(-var13) * Math.PI / 180.0D);
		var13 = (float)Math.sin((double)(-var13) * Math.PI / 180.0D);
		var17 *= var7;
		var4 *= var7;
		var7 = 5.0F;
		float var10001 = var17 * var7;
		float var10002 = var13 * var7;
		var7 = var4 * var7;
		var17 = var10002;
		var13 = var10001;
		Vec3 var14 = new Vec3(var9.x + var13, var9.y + var17, var9.z + var7);
		this.hitResult = this.level.clip(var9, var14);
		checkGlError("Picked");
		this.fogColorMultiplier = 1.0F;
		this.renderDistance = (float)(512 >> (this.levelRenderer.drawDistance << 1));
		GL11.glMatrixMode(RealOpenGLEnums.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, this.renderDistance);
		GL11.glMatrixMode(RealOpenGLEnums.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -0.3F);
		GL11.glRotatef(this.player.xRotO + (this.player.xRot - this.player.xRotO) * var1, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(this.player.yRotO + (this.player.yRot - this.player.yRotO) * var1, 0.0F, 1.0F, 0.0F);
		var7 = this.player.xo + (this.player.x - this.player.xo) * var1;
		var8 = this.player.yo + (this.player.y - this.player.yo) * var1;
		var2 = this.player.zo + (this.player.z - this.player.zo) * var1;
		GL11.glTranslatef(-var7, -var8, -var2);
		checkGlError("Set up camera");
		GL11.glEnable(RealOpenGLEnums.GL_CULL_FACE);
		Frustum var10 = Frustum.getFrustum();
		Frustum var15 = var10;
		LevelRenderer var3 = this.levelRenderer;

		int var18;
		for(var18 = 0; var18 < var3.sortedChunks.length; ++var18) {
			var3.sortedChunks[var18].isInFrustum(var15);
		}

		Player var16 = this.player;
		var3 = this.levelRenderer;
		TreeSet var20 = new TreeSet(new DirtyChunkSorter(var16));
		var20.addAll(var3.dirtyChunks);
		int var21 = 4;
		Iterator var22 = var20.iterator();

		while(var22.hasNext()) {
			Chunk var23 = (Chunk)var22.next();
			var23.rebuild();
			var3.dirtyChunks.remove(var23);
			--var21;
			if(var21 == 0) {
				break;
			}
		}

		checkGlError("Update chunks");
		boolean var11 = this.level.isSolid(this.player.x, this.player.y, this.player.z, 0.1F);
		this.setupFog();
		GL11.glEnable(RealOpenGLEnums.GL_FOG);
		this.levelRenderer.render(this.player, 0);
		int var12;
		if(var11) {
			var12 = (int)this.player.x;
			int var19 = (int)this.player.y;
			var18 = (int)this.player.z;

			for(var21 = var12 - 1; var21 <= var12 + 1; ++var21) {
				for(int var24 = var19 - 1; var24 <= var19 + 1; ++var24) {
					for(int var25 = var18 - 1; var25 <= var18 + 1; ++var25) {
						this.levelRenderer.render(var21, var24, var25);
					}
				}
			}
		}

		checkGlError("Rendered level");
		this.toggleLight(true);
		this.levelRenderer.renderEntities(var10, var1);
		this.toggleLight(false);
		this.setupFog();
		checkGlError("Rendered entities");
		this.particleEngine.render(this.player, var1);
		checkGlError("Rendered particles");
		var3 = this.levelRenderer;
		GL11.glCallList(var3.surroundLists);
		GL11.glDisable(RealOpenGLEnums.GL_LIGHTING);
		this.setupFog();
		this.levelRenderer.renderClouds(var1);
		this.setupFog();
		GL11.glEnable(RealOpenGLEnums.GL_LIGHTING);
		if(this.hitResult != null) {
			GL11.glDisable(RealOpenGLEnums.GL_LIGHTING);
			GL11.glDisable(RealOpenGLEnums.GL_ALPHA_TEST);
			this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.player.inventory.getSelected());
			LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
			GL11.glEnable(RealOpenGLEnums.GL_ALPHA_TEST);
			GL11.glEnable(RealOpenGLEnums.GL_LIGHTING);
		}

		GL11.glBlendFunc(RealOpenGLEnums.GL_SRC_ALPHA, RealOpenGLEnums.GL_ONE_MINUS_SRC_ALPHA);
		this.setupFog();
		var3 = this.levelRenderer;
		GL11.glCallList(var3.surroundLists + 1);
		GL11.glEnable(RealOpenGLEnums.GL_BLEND);
		GL11.glColorMask(false, false, false, false);
		var12 = this.levelRenderer.render(this.player, 1);
		GL11.glColorMask(true, true, true, true);
		if(var12 > 0) {
			var3 = this.levelRenderer;
			GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, var3.textures.getTextureId("/terrain.png"));
			GL11.glCallLists(var3.dummyBuffer);
			GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		}

		GL11.glDepthMask(true);
		GL11.glDisable(RealOpenGLEnums.GL_BLEND);
		GL11.glDisable(RealOpenGLEnums.GL_LIGHTING);
		GL11.glDisable(RealOpenGLEnums.GL_FOG);
		GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
		if(this.hitResult != null) {
			GL11.glDepthFunc(RealOpenGLEnums.GL_LESS);
			GL11.glDisable(RealOpenGLEnums.GL_ALPHA_TEST);
			this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.player.inventory.getSelected());
			LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
			GL11.glEnable(RealOpenGLEnums.GL_ALPHA_TEST);
			GL11.glDepthFunc(RealOpenGLEnums.GL_LEQUAL);
		}

	}

	private void toggleLight(boolean var1) {
		if(!var1) {
			GL11.glDisable(RealOpenGLEnums.GL_LIGHTING);
			GL11.glDisable(RealOpenGLEnums.GL_LIGHT0);
		} else {
			GL11.glEnable(RealOpenGLEnums.GL_LIGHTING);
			GL11.glEnable(RealOpenGLEnums.GL_LIGHT0);
			GL11.glEnable(RealOpenGLEnums.GL_COLOR_MATERIAL);
			GL11.glColorMaterial(RealOpenGLEnums.GL_FRONT_AND_BACK, RealOpenGLEnums.GL_AMBIENT_AND_DIFFUSE);
			float var4 = 0.7F;
			float var2 = 0.3F;
			Vec3 var3 = (new Vec3(0.0F, -1.0F, 0.5F)).normalize();
			GL11.glLight(RealOpenGLEnums.GL_LIGHT0, RealOpenGLEnums.GL_POSITION, this.getBuffer(var3.x, var3.y, var3.z, 0.0F));
			GL11.glLight(RealOpenGLEnums.GL_LIGHT0, RealOpenGLEnums.GL_DIFFUSE, this.getBuffer(var2, var2, var2, 1.0F));
			GL11.glLight(RealOpenGLEnums.GL_LIGHT0, RealOpenGLEnums.GL_AMBIENT, this.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glLightModel(RealOpenGLEnums.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(var4, var4, var4, 1.0F));
		}
	}

	public final void initGui() {
		int var1 = this.width * 240 / this.height;
		int var2 = this.height * 240 / this.height;
		GL11.glClear(RealOpenGLEnums.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(RealOpenGLEnums.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)var1, (double)var2, 0.0D, 100.0D, 300.0D);
		GL11.glMatrixMode(RealOpenGLEnums.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
	}

	private void setupFog() {
		GL11.glFog(RealOpenGLEnums.GL_FOG_COLOR, this.getBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Tile var1 = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)];
		if(var1 != null && var1.getLiquidType() != Liquid.none) {
			Liquid var2 = var1.getLiquidType();
			GL11.glFogi(RealOpenGLEnums.GL_FOG_MODE, RealOpenGLEnums.GL_EXP);
			if(var2 == Liquid.water) {
				GL11.glFogf(RealOpenGLEnums.GL_FOG_DENSITY, 0.1F);
				GL11.glLightModel(RealOpenGLEnums.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.4F, 0.9F, 1.0F));
			} else if(var2 == Liquid.lava) {
				GL11.glFogf(RealOpenGLEnums.GL_FOG_DENSITY, 2.0F);
				GL11.glLightModel(RealOpenGLEnums.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.3F, 0.3F, 1.0F));
			}
		} else {
			GL11.glFogi(RealOpenGLEnums.GL_FOG_MODE, RealOpenGLEnums.GL_LINEAR);
			GL11.glFogf(RealOpenGLEnums.GL_FOG_START, 0.0F);
			GL11.glFogf(RealOpenGLEnums.GL_FOG_END, this.renderDistance);
			GL11.glLightModel(RealOpenGLEnums.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
		}

		GL11.glEnable(RealOpenGLEnums.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(RealOpenGLEnums.GL_FRONT, RealOpenGLEnums.GL_AMBIENT);
		GL11.glEnable(RealOpenGLEnums.GL_LIGHTING);
	}

	private FloatBuffer getBuffer(float var1, float var2, float var3, float var4) {
		this.lb.clear();
		this.lb.put(var1).put(var2).put(var3).put(var4);
		this.lb.flip();
		return this.lb;
	}

	public final void beginLevelLoading(String var1) {
		if(!this.running) {
			throw new StopGameException();
		} else {
			this.title = var1;
			int var3 = this.width * 240 / this.height;
			int var2 = this.height * 240 / this.height;
			GL11.glClear(RealOpenGLEnums.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(RealOpenGLEnums.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)var3, (double)var2, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(RealOpenGLEnums.GL_MODELVIEW);
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
			GL11.glClear(RealOpenGLEnums.GL_DEPTH_BUFFER_BIT | RealOpenGLEnums.GL_COLOR_BUFFER_BIT);
			Tesselator var4 = Tesselator.instance;
			GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			int var5 = this.textures.getTextureId("/dirt.png");
			GL11.glBindTexture(RealOpenGLEnums.GL_TEXTURE_2D, var5);
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
				GL11.glDisable(RealOpenGLEnums.GL_TEXTURE_2D);
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
				GL11.glEnable(RealOpenGLEnums.GL_TEXTURE_2D);
			}

			this.font.drawShadow(this.title, (var2 - this.font.width(this.title)) / 2, var3 / 2 - 4 - 16, 16777215);
			this.font.drawShadow(this.text, (var2 - this.font.width(this.text)) / 2, var3 / 2 - 4 + 8, 16777215);
			Display.update();

			try {
				Thread.yield();
			} catch (Exception var7) {
			}
		}
	}

	public final void generateLevel(int var1) {
		String var2 = this.user != null ? this.user.name : "anonymous";
		this.setLevel(this.levelGen.generateLevel(var2, 128 << var1, 128 << var1, 64));
	}

	public final boolean loadLevel(String var1, int var2) {
		Level var3 = this.levelIo.load(this.minecraftUri, var1, var2);
		boolean var4 = var3 != null;
		if(!var4) {
			return false;
		} else {
			this.setLevel(var3);
			return true;
		}
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
