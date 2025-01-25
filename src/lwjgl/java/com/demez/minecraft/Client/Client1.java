package com.demez.minecraft.Client;

import com.mojang.minecraft.Minecraft;

public class Client1 {
    public static void main(String[] args) {
        // Create an instance of the Minecraft class
        Minecraft minecraft = new Minecraft(1000, 600, false);

        // Start the Minecraft instance in a separate thread
        Thread gameThread = new Thread(minecraft);
        gameThread.start();
    }
}
