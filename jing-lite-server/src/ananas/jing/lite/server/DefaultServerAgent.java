package ananas.jing.lite.server;

import java.io.File;

import ananas.jing.lite.core.JingEndpointFactory;
import ananas.jing.lite.core.server.JingServer;

public class DefaultServerAgent implements ServerAgent {

	@Override
	public JingServer getServer() {

		File repo = new File("/var/lib/ananas/jing/server/repo/");
		String url = "http://localhost:8980/jing-lite-server/";

		JingEndpointFactory factory = JingEndpointFactory.Agent.getInstance();
		return factory.newServer(repo, url);
	}

}
