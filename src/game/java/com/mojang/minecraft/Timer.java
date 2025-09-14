package com.mojang.minecraft;

public final class Timer {
	float ticksPerSecond;
	double lastHRTime;
	public int ticks;
	public float a;
	public float timeScale = 1.0F;
	public float fps = 0.0F;
	long lastSyncSysClock;
	long lastSyncHRClock;
	double timeSyncAdjustment = 1.0D;

	public Timer(float var1) {
		this.ticksPerSecond = var1;
		this.lastSyncSysClock = System.currentTimeMillis();
		this.lastSyncHRClock = System.nanoTime() / 1000000L;
	}
}
