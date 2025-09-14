package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.tile.Tile;
import java.util.ArrayList;
import java.util.Random;

public final class LevelGen {
	private Minecraft minecraft;
	private int width;
	private int height;
	private int depth;
	private Random random = new Random();
	private byte[] blocks;
	private int[] coords = new int[1048576];

	public LevelGen(Minecraft var1) {
		this.minecraft = var1;
	}

	public final Level generateLevel(String var1, int var2, int var3, int var4) {
		this.minecraft.beginLevelLoading("Generating level");
		this.width = var2;
		this.height = var3;
		this.depth = 64;
		this.blocks = new byte[var2 * var3 << 6];
		this.minecraft.levelLoadUpdate("Raising..");
		LevelGen var5 = this;
		Distort var8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort var9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise var10 = new PerlinNoise(this.random, 8);
		int[] var11 = new int[this.width * this.height];
		float var6 = 1.3F;

		int var13;
		int var14;
		for(var13 = 0; var13 < var5.width; ++var13) {
			var5.setNextPhase(var13 * 100 / (var5.width - 1));

			for(var14 = 0; var14 < var5.height; ++var14) {
				double var15 = var8.getValue((double)((float)var13 * var6), (double)((float)var14 * var6)) / 8.0D - 8.0D;
				double var17 = var9.getValue((double)((float)var13 * var6), (double)((float)var14 * var6)) / 6.0D + 6.0D;
				double var19 = var10.getValue((double)var13, (double)var14) / 8.0D;
				if(var19 > 0.0D) {
					var17 = var15;
				}

				double var21 = Math.max(var15, var17) / 2.0D;
				if(var21 < 0.0D) {
					var21 *= 0.8D;
				}

				var11[var13 + var14 * var5.width] = (int)var21;
			}
		}

		this.minecraft.levelLoadUpdate("Eroding..");
		int[] var34 = var11;
		var5 = this;
		var9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort var40 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int var16;
		int var32;
		int var43;
		int var46;
		for(var43 = 0; var43 < var5.width; ++var43) {
			var5.setNextPhase(var43 * 100 / (var5.width - 1));

			for(var32 = 0; var32 < var5.height; ++var32) {
				double var45 = var9.getValue((double)(var43 << 1), (double)(var32 << 1)) / 8.0D;
				var46 = var40.getValue((double)(var43 << 1), (double)(var32 << 1)) > 0.0D ? 1 : 0;
				if(var45 > 2.0D) {
					var16 = var34[var43 + var32 * var5.width];
					var16 = ((var16 - var46) / 2 << 1) + var46;
					var34[var43 + var32 * var5.width] = var16;
				}
			}
		}

		this.minecraft.levelLoadUpdate("Soiling..");
		var34 = var11;
		var5 = this;
		int var37 = this.width;
		int var42 = this.height;
		var43 = this.depth;
		PerlinNoise var33 = new PerlinNoise(this.random, 8);

		int var18;
		int var50;
		for(var13 = 0; var13 < var37; ++var13) {
			var5.setNextPhase(var13 * 100 / (var5.width - 1));

			for(var14 = 0; var14 < var42; ++var14) {
				var46 = (int)(var33.getValue((double)var13, (double)var14) / 24.0D) - 4;
				var16 = var34[var13 + var14 * var37] + var43 / 2;
				var50 = var16 + var46;
				var34[var13 + var14 * var37] = Math.max(var16, var50);

				for(var18 = 0; var18 < var43; ++var18) {
					int var52 = (var18 * var5.height + var14) * var5.width + var13;
					int var20 = 0;
					if(var18 <= var16) {
						var20 = Tile.dirt.id;
					}

					if(var18 <= var50) {
						var20 = Tile.rock.id;
					}

					var5.blocks[var52] = (byte)var20;
				}
			}
		}

		this.minecraft.levelLoadUpdate("Carving..");
		boolean var39 = true;
		boolean var35 = false;
		var5 = this;
		var42 = this.width;
		var43 = this.height;
		var32 = this.depth;
		var13 = var42 * var43 * var32 / 256 / 64;

		for(var14 = 0; var14 < var13; ++var14) {
			var5.setNextPhase(var14 * 100 / (var13 - 1) / 4);
			float var47 = var5.random.nextFloat() * (float)var42;
			float var48 = var5.random.nextFloat() * (float)var32;
			float var51 = var5.random.nextFloat() * (float)var43;
			var18 = (int)((var5.random.nextFloat() + var5.random.nextFloat()) * 75.0F);
			float var53 = (float)((double)var5.random.nextFloat() * Math.PI * 2.0D);
			float var54 = 0.0F;
			float var55 = (float)((double)var5.random.nextFloat() * Math.PI * 2.0D);
			float var22 = 0.0F;

			for(int var7 = 0; var7 < var18; ++var7) {
				var47 = (float)((double)var47 + Math.sin((double)var53) * Math.cos((double)var55));
				var51 = (float)((double)var51 + Math.cos((double)var53) * Math.cos((double)var55));
				var48 = (float)((double)var48 + Math.sin((double)var55));
				var53 += var54 * 0.2F;
				var54 *= 0.9F;
				var54 += var5.random.nextFloat() - var5.random.nextFloat();
				var55 += var22 * 0.5F;
				var55 *= 0.5F;
				var22 *= 0.9F;
				var22 += var5.random.nextFloat() - var5.random.nextFloat();
				if(var5.random.nextFloat() >= 0.3F) {
					float var36 = var47 + var5.random.nextFloat() * 4.0F - 2.0F;
					float var41 = var48 + var5.random.nextFloat() * 4.0F - 2.0F;
					float var12 = var51 + var5.random.nextFloat() * 4.0F - 2.0F;
					float var23 = (float)(Math.sin((double)var7 * Math.PI / (double)var18) * 2.5D + 1.0D);

					for(int var24 = (int)(var36 - var23); var24 <= (int)(var36 + var23); ++var24) {
						for(int var25 = (int)(var41 - var23); var25 <= (int)(var41 + var23); ++var25) {
							for(int var26 = (int)(var12 - var23); var26 <= (int)(var12 + var23); ++var26) {
								float var27 = (float)var24 - var36;
								float var28 = (float)var25 - var41;
								float var29 = (float)var26 - var12;
								var27 = var27 * var27 + var28 * var28 * 2.0F + var29 * var29;
								if(var27 < var23 * var23 && var24 >= 1 && var25 >= 1 && var26 >= 1 && var24 < var5.width - 1 && var25 < var5.depth - 1 && var26 < var5.height - 1) {
									int var56 = (var25 * var5.height + var26) * var5.width + var24;
									if(var5.blocks[var56] == Tile.rock.id) {
										var5.blocks[var56] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.carveTunnels(Tile.oreCoal.id, 90, 1, 4);
		this.carveTunnels(Tile.oreIron.id, 70, 2, 4);
		this.carveTunnels(Tile.oreGold.id, 50, 3, 4);
		this.minecraft.levelLoadUpdate("Watering..");
		var5 = this;
		long var38 = System.nanoTime();
		long var44 = 0L;
		var13 = Tile.calmWater.id;
		this.setNextPhase(0);

		for(var14 = 0; var14 < var5.width; ++var14) {
			var44 += var5.floodFillLiquid(var14, var5.depth / 2 - 1, 0, 0, var13);
			var44 += var5.floodFillLiquid(var14, var5.depth / 2 - 1, var5.height - 1, 0, var13);
		}

		for(var14 = 0; var14 < var5.height; ++var14) {
			var44 += var5.floodFillLiquid(0, var5.depth / 2 - 1, var14, 0, var13);
			var44 += var5.floodFillLiquid(var5.width - 1, var5.depth / 2 - 1, var14, 0, var13);
		}

		var14 = var5.width * var5.height / 200;

		for(var46 = 0; var46 < var14; ++var46) {
			if(var46 % 100 == 0) {
				var5.setNextPhase(var46 * 100 / (var14 - 1));
			}

			var16 = var5.random.nextInt(var5.width);
			var50 = var5.depth / 2 - 1 - var5.random.nextInt(3);
			var18 = var5.random.nextInt(var5.height);
			if(var5.blocks[(var50 * var5.height + var18) * var5.width + var16] == 0) {
				var44 += var5.floodFillLiquid(var16, var50, var18, 0, var13);
			}
		}

		var5.setNextPhase(100);
		long var49 = System.nanoTime();
		System.out.println("Flood filled " + var44 + " tiles in " + (double)(var49 - var38) / 1000000.0D + " ms");
		this.minecraft.levelLoadUpdate("Melting..");
		this.addLava();
		this.minecraft.levelLoadUpdate("Growing..");
		this.addBeaches(var11);
		this.minecraft.levelLoadUpdate("Planting..");
		this.plantTrees(var11);
		Level var31 = new Level();
		var31.setData(var2, 64, var3, this.blocks);
		var31.createTime = System.currentTimeMillis();
		var31.creator = var1;
		var31.name = "A Nice World";
		return var31;
	}

	private void addBeaches(int[] var1) {
		int var2 = this.width;
		int var3 = this.height;
		int var4 = this.depth;
		PerlinNoise var5 = new PerlinNoise(this.random, 8);
		PerlinNoise var6 = new PerlinNoise(this.random, 8);

		for(int var7 = 0; var7 < var2; ++var7) {
			this.setNextPhase(var7 * 100 / (this.width - 1));

			for(int var8 = 0; var8 < var3; ++var8) {
				boolean var9 = var5.getValue((double)var7, (double)var8) > 8.0D;
				boolean var10 = var6.getValue((double)var7, (double)var8) > 12.0D;
				int var11 = var1[var7 + var8 * var2];
				int var12 = (var11 * this.height + var8) * this.width + var7;
				int var13 = this.blocks[((var11 + 1) * this.height + var8) * this.width + var7] & 255;
				if((var13 == Tile.water.id || var13 == Tile.calmWater.id) && var11 <= var4 / 2 - 1 && var10) {
					this.blocks[var12] = (byte)Tile.gravel.id;
				}

				if(var13 == 0) {
					int var14 = Tile.grass.id;
					if(var11 <= var4 / 2 - 1 && var9) {
						var14 = Tile.sand.id;
					}

					this.blocks[var12] = (byte)var14;
				}
			}
		}

	}

	private void plantTrees(int[] var1) {
		int var2 = this.width;
		int var3 = this.width * this.height / 4000;

		for(int var4 = 0; var4 < var3; ++var4) {
			this.setNextPhase(var4 * 100 / (var3 - 1));
			int var5 = this.random.nextInt(this.width);
			int var6 = this.random.nextInt(this.height);

			for(int var7 = 0; var7 < 20; ++var7) {
				int var8 = var5;
				int var9 = var6;

				for(int var10 = 0; var10 < 20; ++var10) {
					var8 += this.random.nextInt(6) - this.random.nextInt(6);
					var9 += this.random.nextInt(6) - this.random.nextInt(6);
					if(var8 >= 0 && var9 >= 0 && var8 < this.width && var9 < this.height) {
						int var11 = var1[var8 + var9 * var2] + 1;
						int var12 = this.random.nextInt(3) + 4;
						boolean var13 = true;

						int var14;
						int var16;
						int var17;
						int var18;
						for(var14 = var11; var14 <= var11 + 1 + var12; ++var14) {
							byte var15 = 1;
							if(var14 >= var11 + 1 + var12 - 2) {
								var15 = 2;
							}

							for(var16 = var8 - var15; var16 <= var8 + var15 && var13; ++var16) {
								for(var17 = var9 - var15; var17 <= var9 + var15 && var13; ++var17) {
									if(var16 >= 0 && var14 >= 0 && var17 >= 0 && var16 < this.width && var14 < this.depth && var17 < this.height) {
										var18 = this.blocks[(var14 * this.height + var17) * this.width + var16] & 255;
										if(var18 != 0) {
											var13 = false;
										}
									} else {
										var13 = false;
									}
								}
							}
						}

						if(var13) {
							var14 = (var11 * this.height + var9) * this.width + var8;
							int var22 = this.blocks[((var11 - 1) * this.height + var9) * this.width + var8] & 255;
							if(var22 == Tile.grass.id && var11 < this.depth - var12 - 1) {
								this.blocks[var14 - 1 * this.width * this.height] = (byte)Tile.dirt.id;

								for(var16 = var11 - 3 + var12; var16 <= var11 + var12; ++var16) {
									var17 = var16 - (var11 + var12);
									var18 = 1 - var17 / 2;

									for(int var21 = var8 - var18; var21 <= var8 + var18; ++var21) {
										var22 = var21 - var8;

										for(int var19 = var9 - var18; var19 <= var9 + var18; ++var19) {
											int var20 = var19 - var9;
											if(Math.abs(var22) != var18 || Math.abs(var20) != var18 || this.random.nextInt(2) != 0 && var17 != 0) {
												this.blocks[(var16 * this.height + var19) * this.width + var21] = (byte)Tile.leaf.id;
											}
										}
									}
								}

								for(var16 = 0; var16 < var12; ++var16) {
									this.blocks[var14 + var16 * this.width * this.height] = (byte)Tile.log.id;
								}
							}
						}
					}
				}
			}
		}

	}

	private void carveTunnels(int var1, int var2, int var3, int var4) {
		byte var25 = (byte)var1;
		var4 = this.width;
		int var5 = this.height;
		int var6 = this.depth;
		int var7 = var4 * var5 * var6 / 256 / 64 * var2 / 100;

		for(int var8 = 0; var8 < var7; ++var8) {
			this.setNextPhase(var8 * 100 / (var7 - 1) / 4 + var3 * 100 / 4);
			float var9 = this.random.nextFloat() * (float)var4;
			float var10 = this.random.nextFloat() * (float)var6;
			float var11 = this.random.nextFloat() * (float)var5;
			int var12 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)var2 / 100.0F);
			float var13 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float var14 = 0.0F;
			float var15 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float var16 = 0.0F;

			for(int var17 = 0; var17 < var12; ++var17) {
				var9 = (float)((double)var9 + Math.sin((double)var13) * Math.cos((double)var15));
				var11 = (float)((double)var11 + Math.cos((double)var13) * Math.cos((double)var15));
				var10 = (float)((double)var10 + Math.sin((double)var15));
				var13 += var14 * 0.2F;
				var14 *= 0.9F;
				var14 += this.random.nextFloat() - this.random.nextFloat();
				var15 += var16 * 0.5F;
				var15 *= 0.5F;
				var16 *= 0.9F;
				var16 += this.random.nextFloat() - this.random.nextFloat();
				float var18 = (float)(Math.sin((double)var17 * Math.PI / (double)var12) * (double)var2 / 100.0D + 1.0D);

				for(int var19 = (int)(var9 - var18); var19 <= (int)(var9 + var18); ++var19) {
					for(int var20 = (int)(var10 - var18); var20 <= (int)(var10 + var18); ++var20) {
						for(int var21 = (int)(var11 - var18); var21 <= (int)(var11 + var18); ++var21) {
							float var22 = (float)var19 - var9;
							float var23 = (float)var20 - var10;
							float var24 = (float)var21 - var11;
							var22 = var22 * var22 + var23 * var23 * 2.0F + var24 * var24;
							if(var22 < var18 * var18 && var19 >= 1 && var20 >= 1 && var21 >= 1 && var19 < this.width - 1 && var20 < this.depth - 1 && var21 < this.height - 1) {
								int var26 = (var20 * this.height + var21) * this.width + var19;
								if(this.blocks[var26] == Tile.rock.id) {
									this.blocks[var26] = var25;
								}
							}
						}
					}
				}
			}
		}

	}

	private void setNextPhase(int var1) {
		this.minecraft.setLoadingProgress(var1);
	}

	private void addLava() {
		int var1 = 0;
		int var2 = this.width * this.height * this.depth / 10000;

		for(int var3 = 0; var3 < var2; ++var3) {
			if(var3 % 100 == 0) {
				this.setNextPhase(var3 * 100 / (var2 - 1));
			}

			int var4 = this.random.nextInt(this.width);
			int var5 = this.random.nextInt(this.depth / 2 - 4);
			int var6 = this.random.nextInt(this.height);
			if(this.blocks[(var5 * this.height + var6) * this.width + var4] == 0) {
				++var1;
				this.floodFillLiquid(var4, var5, var6, 0, Tile.calmLava.id);
			}
		}

		this.setNextPhase(100);
		System.out.println("LavaCount: " + var1);
	}

	private long floodFillLiquid(int var1, int var2, int var3, int var4, int var5) {
		byte var20 = (byte)var5;
		ArrayList var21 = new ArrayList();
		byte var6 = 0;
		int var7 = 1;

		int var8;
		for(var8 = 1; 1 << var7 < this.width; ++var7) {
		}

		while(1 << var8 < this.height) {
			++var8;
		}

		int var9 = this.height - 1;
		int var10 = this.width - 1;
		int var22 = var6 + 1;
		this.coords[0] = ((var2 << var8) + var3 << var7) + var1;
		long var13 = 0L;
		var1 = this.width * this.height;

		while(var22 > 0) {
			--var22;
			var2 = this.coords[var22];
			if(var22 == 0 && var21.size() > 0) {
				System.out.println("IT HAPPENED!");
				this.coords = (int[])var21.remove(var21.size() - 1);
				var22 = this.coords.length;
			}

			var3 = var2 >> var7 & var9;
			int var11 = var2 >> var7 + var8;
			int var12 = var2 & var10;

			int var15;
			for(var15 = var12; var12 > 0 && this.blocks[var2 - 1] == 0; --var2) {
				--var12;
			}

			while(var15 < this.width && this.blocks[var2 + var15 - var12] == 0) {
				++var15;
			}

			int var16 = var2 >> var7 & var9;
			int var17 = var2 >> var7 + var8;
			if(var16 != var3 || var17 != var11) {
				System.out.println("hoooly fuck");
			}

			boolean var23 = false;
			boolean var24 = false;
			boolean var18 = false;
			var13 += (long)(var15 - var12);

			for(var12 = var12; var12 < var15; ++var12) {
				this.blocks[var2] = var20;
				boolean var19;
				if(var3 > 0) {
					var19 = this.blocks[var2 - this.width] == 0;
					if(var19 && !var23) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - this.width;
					}

					var23 = var19;
				}

				if(var3 < this.height - 1) {
					var19 = this.blocks[var2 + this.width] == 0;
					if(var19 && !var24) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 + this.width;
					}

					var24 = var19;
				}

				if(var11 > 0) {
					byte var25 = this.blocks[var2 - var1];
					if((var20 == Tile.lava.id || var20 == Tile.calmLava.id) && (var25 == Tile.water.id || var25 == Tile.calmWater.id)) {
						this.blocks[var2 - var1] = (byte)Tile.rock.id;
					}

					var19 = var25 == 0;
					if(var19 && !var18) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - var1;
					}

					var18 = var19;
				}

				++var2;
			}
		}

		return var13;
	}
}
