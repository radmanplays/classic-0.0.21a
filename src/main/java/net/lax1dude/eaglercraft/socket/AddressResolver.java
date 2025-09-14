/*
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.socket;

import net.lax1dude.eaglercraft.EagRuntime;

import java.net.URI;
import java.net.URISyntaxException;

public class AddressResolver {
	public static class ServerInfo {
		public String host;
		public int port;
		public String ip;

        public ServerInfo(String host, int port, String ip) {
            this.host = host;
            this.port = port;
			this.ip = ip;
        }
	}
	
	public static ServerInfo resolveURI(String input) {
		String lc = input.toLowerCase();
		if (!lc.startsWith("ws://") && !lc.startsWith("wss://")) {
			if (EagRuntime.requireSSL()) {
				input = "wss://" + input;
			} else {
				input = "ws://" + input;
			}
		}
		try {
			URI uri = new URI(input);

			String host = uri.getHost();
			int port = uri.getPort();

			if (port == -1) {
				port = "wss".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
			}

			return new ServerInfo(host, port, input);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return new ServerInfo("127.0.0.1", 8080, "ws://127.0.0.1:8080");
		}
	}
}