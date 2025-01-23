package com.mojang.minecraft.net;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

final class NetworkSkinDownloadThread extends Thread {
	private NetworkPlayer player;

	NetworkSkinDownloadThread(NetworkPlayer var1) {
		this.player = var1;
	}

	public final void run() {
		HttpURLConnection var1 = null;

		try {
			URL var2 = new URL("http://www.minecraft.net/skin/" + this.player.name + ".png");
			var1 = (HttpURLConnection)var2.openConnection();
			var1.setDoInput(true);
			var1.setDoOutput(false);
			var1.connect();
			if(var1.getResponseCode() != 404) {
				System.out.println("Loading texture for " + this.player.name);
				this.player.newTexture = ImageIO.read(var1.getInputStream());
				return;
			}

			System.out.println("Failed to load texture for " + this.player.name);
		} catch (Exception var5) {
			var5.printStackTrace();
			return;
		} finally {
			var1.disconnect();
		}

	}
}
