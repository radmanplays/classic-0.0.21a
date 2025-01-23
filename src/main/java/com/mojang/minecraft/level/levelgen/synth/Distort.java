package com.mojang.minecraft.level.levelgen.synth;

public final class Distort extends Synth {
	private Synth source;
	private Synth distort;

	public Distort(Synth var1, Synth var2) {
		this.source = var1;
		this.distort = var2;
	}

	public final double getValue(double var1, double var3) {
		return this.source.getValue(var1 + this.distort.getValue(var1, var3), var3);
	}
}
